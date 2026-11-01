package com.example.core.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AnalyticsDao {

    @Query("select SUM(distanceMeters) from runentity")
    suspend fun getTotalDistance(): Int


    @Query("select SUM(durationMillis) from runentity")
    suspend fun getTotalTimeRun(): Long

    @Query("select MAX(maxSpeedKmh) from runentity")
    suspend fun getMaxRunSpeed(): Double

    @Query("select AVG(distanceMeters) from runentity")
    suspend fun getAvgDistancePerRun(): Double

    @Query("select AVG((durationMillis /6000.0) / (distanceMeters / 1000.0)  ) from runentity")
    suspend fun getAvgPace(): Double
}