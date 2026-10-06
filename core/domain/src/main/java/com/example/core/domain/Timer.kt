package com.example.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

object Timer {
    fun timeAndEmit(): Flow<Duration>{
        return flow {
            var lastEmitTime = System.currentTimeMillis()

            while (true){
                val current = System.currentTimeMillis()
                val elapsedTime = current - lastEmitTime
                emit(elapsedTime.milliseconds)
                lastEmitTime = current
            }
        }
    }
}