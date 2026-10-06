package com.example.run.presentation.run_active

import com.example.core.domain.location.Location
import com.example.run.domain.RunData
import kotlin.time.Duration

/// data we need to update from viewmodel to screen
data class ActiveRunState(
    val elapsedTime: Duration= Duration.ZERO,
    val shouldTrack: Boolean = false,
    val hasStartedRunning: Boolean = false,
    val location: Location? = null,
    val isRunFinished: Boolean = false,
    val isRunSaved: Boolean = false,
    val isSavingRun: Boolean = false,
    val runData: RunData = RunData(),
    val showLocationRationale: Boolean = false,
    val showNotificationRationale: Boolean = false
)
