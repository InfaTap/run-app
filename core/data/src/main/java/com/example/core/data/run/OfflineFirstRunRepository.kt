package com.example.core.data.run

import com.example.core.database.dao.RunPendingSyncDao
import com.example.core.database.mapper.toRun
import com.example.core.domain.SessionStorage
import com.example.core.domain.run.LocalRunDataSource
import com.example.core.domain.run.RemoteRunDataSource
import com.example.core.domain.run.Run
import com.example.core.domain.run.RunId
import com.example.core.domain.run.RunRepository
import com.example.core.domain.run.SyncRunScheduler
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyDataResult
import com.example.core.domain.util.Result
import com.example.core.domain.util.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfflineFirstRunRepository(
    private val localRunDataSource: LocalRunDataSource,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val applicationScope: CoroutineScope,
    private val syncDao: RunPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val syncRunScheduler: SyncRunScheduler
) : RunRepository {
    override fun getRuns(): Flow<List<Run>> {
        return localRunDataSource.getRuns()
    }

    override suspend fun fetchRuns(): EmptyDataResult<DataError> {
        return when (val result = remoteRunDataSource.getRuns()) {
            is Result.Error -> result.asEmptyDataResult()
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRuns(result.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun upsertRun(run: Run, mapPicture: ByteArray): EmptyDataResult<DataError> {
        val localRun = localRunDataSource.upsertRun(run)
        if (localRun !is Result.Success) {
            return localRun.asEmptyDataResult()
        }
        val remoteRun = run.copy(id = localRun.data)
        val result = remoteRunDataSource.postRun(remoteRun, mapPicture)

        return when (result) {
            is Result.Error -> {
                applicationScope.launch {
                    syncRunScheduler.scheduleSync(
                        type = SyncRunScheduler.SyncType.CreateRun(
                            run = remoteRun,
                            mapPicture
                        )
                    )
                }.join()

                Result.Success(Unit)
            }

            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRun(result.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun deleteRun(id: RunId) {
        localRunDataSource.deleteRun(id)

        val isPendingSync = syncDao.getRunPendingSync(id) != null
        if (isPendingSync) {
            syncDao.deleteDeletedPendingRunSync(id)
            return
        }

        val remoteResult = applicationScope.async {
            remoteRunDataSource.deleteRun(id)
        }.await()

        if (remoteResult is Result.Error) {
            applicationScope.launch {
                syncRunScheduler.scheduleSync(SyncRunScheduler.SyncType.DeleteRun(id))
            }.join()
        }
    }

    override suspend fun syncPendingRuns() {
        withContext(Dispatchers.IO) {
            val userId = sessionStorage.getAuthInfo()?.userId ?: return@withContext


            val createdRuns = async {
                syncDao.getAllRunPendingSync(userId)
            }
            val deletedRun = async {
                syncDao.getAllDeletePendingSync(userId)
            }

            val createJob = createdRuns
                .await()
                .map {
                    launch {
                        val run = it.runEntity.toRun()
                        when (remoteRunDataSource.postRun(run, it.mapPictureByteArray)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    syncDao.deleteRunPendingSync(it.runId)
                                }.join()
                            }
                        }
                    }
                }

            val deleteJob = deletedRun
                .await()
                .map {
                    launch {
                        when (remoteRunDataSource.deleteRun(it.runId)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    syncDao.deleteDeletedPendingRunSync(it.runId)
                                }.join()
                            }
                        }
                    }
                }

            createJob.joinAll()
            deleteJob.joinAll()
        }
    }
}