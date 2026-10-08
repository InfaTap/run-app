package com.example.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.core.database.dao.RunPendingSyncDao
import com.example.core.database.entity.RunPendingSyncEntity
import com.example.core.database.mapper.toRun
import com.example.core.domain.run.RemoteRunDataSource

class CreateRunWorker(
    context: Context,
    val params: WorkerParameters,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val pendingSyncDao: RunPendingSyncDao
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        val pendingRunId = params.inputData.getString(RUN_ID) ?: return Result.failure()
        val pendingEntity =
            pendingSyncDao.getRunPendingSync(pendingRunId) ?: return Result.failure()

        val run = pendingEntity.runEntity.toRun()

        return when (val result =
            remoteRunDataSource.postRun(run, pendingEntity.mapPictureByteArray)) {
            is com.example.core.domain.util.Result.Error -> {
                result.error.toWorkerResult()
            }

            is com.example.core.domain.util.Result.Success -> {
                pendingSyncDao.deleteRunPendingSync(pendingRunId)
                Result.success()
            }
        }
    }

    companion object {
        const val RUN_ID = "RUN_ID"
    }
}