package com.example.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.core.database.entity.DeletedRunSyncEntity
import com.example.core.database.entity.RunPendingSyncEntity

@Dao
interface RunPendingSyncDao {

    @Query("select * from runpendingsyncentity where userId=:userId")
    suspend fun getAllRunPendingSync(userId: String): List<RunPendingSyncEntity>

    @Query("select * from runpendingsyncentity where runId=:runId")
    suspend fun getRunPendingSync(runId: String): RunPendingSyncEntity?

    @Upsert
    suspend fun upsertRunPendingSync(runPendingSyncEntity: RunPendingSyncEntity)

    @Query("delete from runpendingsyncentity where runId=:runId")
    suspend fun deleteRunPendingSync(runId: String)


    @Query("select * from deletedrunsyncentity where userId=:userId")
    suspend fun getAllDeletePendingSync(userId: String): List<DeletedRunSyncEntity>

    @Upsert
    suspend fun upsertOrAddDeletePendingSync(deletedRunSyncEntity: DeletedRunSyncEntity)

    @Query("delete from deletedrunsyncentity where runId=:runId")
    suspend fun deleteDeletedPendingRunSync(runId: String)
}