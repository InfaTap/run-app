package com.example.analytics.data

import com.example.analytics.domain.AnalyticsValues
import com.example.analytics.domain.AnalyticsRepository
import com.example.core.database.dao.AnalyticsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class RoomAnalyticsRepository(private val analyticsDao: AnalyticsDao) : AnalyticsRepository {
    override suspend fun getAnalyticsValues(): AnalyticsValues {

        return withContext(Dispatchers.IO){
            val totalDistance = async { analyticsDao.getTotalDistance() }
            val totalTimeRun = async { analyticsDao.getTotalTimeRun() }
            val avgDistancePerRun = async { analyticsDao.getAvgDistancePerRun() }
            val avgPace= async { analyticsDao.getAvgPace() }
            val maxRunSpeed = async { analyticsDao.getMaxRunSpeed() }

            AnalyticsValues(
                totalDistanceRun = totalDistance.await(),
                totalTimeRun = totalTimeRun.await().milliseconds,
                fastestRunEver = maxRunSpeed.await(),
                avgPacePerRun = avgPace.await(),
                avgDistancePerRun = avgDistancePerRun.await()

            )
        }



    }
}