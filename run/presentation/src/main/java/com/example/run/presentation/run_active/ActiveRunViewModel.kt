package com.example.run.presentation.run_active

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.run.domain.RunningTracker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class ActiveRunViewModel(private val runningTracker: RunningTracker) : ViewModel() {


    private val eventChannel = Channel<ActiveRunEvent>()
    val events = eventChannel.receiveAsFlow()

    private val hasLocationPermission = MutableStateFlow(false)
    var state by mutableStateOf(ActiveRunState())
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

            ActiveRunAction.OnFinishRunClick -> {}
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

            else -> {}
        }
    }
}