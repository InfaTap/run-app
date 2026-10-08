package com.example.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.core.database.entity.RunEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {

    @Upsert
    suspend fun upsertRun(run: RunEntity)

    @Upsert
    suspend fun upsertRuns(runs: List<RunEntity>)

    @Query("Select * from runentity order by dateTimeUtc desc")
    fun getRuns(): Flow<List<RunEntity>>

    @Query("delete from runentity where id=:id")
    suspend fun deleteRun(id: String)

    @Query("delete from runentity")
    suspend fun deleteAllRuns()
}