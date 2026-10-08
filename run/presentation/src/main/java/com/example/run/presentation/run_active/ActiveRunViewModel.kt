package com.example.run.presentation.run_active

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.LocationDataCalculator
import com.example.core.domain.location.Location
import com.example.core.domain.run.Run
import com.example.run.domain.RunningTracker
import com.example.run.presentation.run_active.service.ActiveRunService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime

class ActiveRunViewModel(private val runningTracker: RunningTracker) : ViewModel() {


    private val eventChannel = Channel<ActiveRunEvent>()
    val events = eventChannel.receiveAsFlow()

    private val hasLocationPermission = MutableStateFlow(false)
    var state by mutableStateOf(ActiveRunState(
        shouldTrack = ActiveRunService.isServiceActive && runningTracker.isTracking.value,
        hasStartedRunning = ActiveRunService.isServiceActive
    ))
        private set

    private val shouldTrack = snapshotFlow { state.shouldTrack }.stateIn(
        scope = viewModelScope, SharingStarted.Lazily, state.shouldTrack
    )
    private val isTracking =
        combine(shouldTrack, hasLocationPermission) { shouldTrack, hasPermission ->
            shouldTrack && hasPermission
        }.stateIn(viewModelScope, SharingStarted.Lazily, false)


    init {
        hasLocationPermission.onEach { hasPermission ->
            if (hasPermission) {
                runningTracker.startObserving()
            } else {
                runningTracker.stopObserving()
            }
        }.launchIn(viewModelScope)


        isTracking.onEach { isTracking ->
            runningTracker.setIsTracking(isTracking)

        }.launchIn(viewModelScope)

        runningTracker
            .currentLocation
            .onEach {
              state =  state.copy(
                  location = it?.location
              )
            }.launchIn(viewModelScope)

        runningTracker
            .runData
            .onEach {
                state = state.copy(
                    runData = it
                )
            }.launchIn(viewModelScope)

        runningTracker
            .elapsedTime
            .onEach {
                state = state.copy(
                    elapsedTime = it
                )
            }.launchIn(viewModelScope)
    }



    fun onAction(action: ActiveRunAction) {
        when (action) {

            ActiveRunAction.OnFinishRunClick -> {
                state = state.copy(
                    isRunFinished = true,
                    isSavingRun = true
                )
            }
            ActiveRunAction.OnResumeClick -> {
                state = state.copy(shouldTrack = true)
            }
            ActiveRunAction.OnBackClick -> {
                state = state.copy(shouldTrack = false)
            }
            ActiveRunAction.OnToggleRunClick -> {
                state = state.copy(
                    hasStartedRunning = true,
                    shouldTrack = !state.shouldTrack
                )
            }
            is ActiveRunAction.ShowLocationPermissionInfo -> {
                hasLocationPermission.value = action.acceptedLocationPermission
                state = state.copy(
                    showLocationRationale = action.showLocationRationale
                )
            }

            is ActiveRunAction.ShowNotificationPermissionInfo -> {
                state = state.copy(
                    showNotificationRationale = action.acceptedNotificationRationale
                )
            }

            is ActiveRunAction.DismissRationalDialog -> {
                state = state.copy(
                    showLocationRationale = false,
                    showNotificationRationale = false
                )
            }
            is ActiveRunAction.OnRunProcessed -> {
                finishRun(action.mapPictureByteArray)
            }

            else -> {}
        }
    }

    private fun finishRun(mapPictureByteArray: ByteArray) {
        val locations = state.runData.locations
        if (locations.isEmpty() || locations.first().size <= 1){
            state = state.copy(
                isSavingRun = false
            )
            return
        }
        viewModelScope.launch {
            val run = Run(
                id = null,
                duration = state.elapsedTime,
                dateTimeUtc = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")),
                distanceInMeters = state.runData.distanceMeters,
                location = state.location ?: Location(0.0,0.0),
                maxSpeedKm = LocationDataCalculator.getMaxSpeedKmh(locations)  ,
                totalElevationInMeters = LocationDataCalculator.getTotalElevationInMeters(locations),
                mapPictureUrl = null,
            )
        }

        runningTracker.finishRun()
    }

    override fun onCleared() {
        super.onCleared()
        if (!ActiveRunService.isServiceActive){
            runningTracker.stopObserving()
        }
    }
}