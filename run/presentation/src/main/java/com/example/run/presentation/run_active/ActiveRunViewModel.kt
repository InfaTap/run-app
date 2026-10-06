package com.example.run.presentation.run_active

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.run.domain.RunningTracker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber

class ActiveRunViewModel(private val runningTracker: RunningTracker) : ViewModel() {


    private val eventChannel = Channel<ActiveRunEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _hasLocationPermission = MutableStateFlow(false)


    init {
        _hasLocationPermission.onEach { hasPermission ->
            if (hasPermission) {
                runningTracker.startObserving()
            } else {
                runningTracker.startObserving()
            }
        }.launchIn(viewModelScope)

        runningTracker
            .currentLocation
            .onEach { location ->
                Timber.d("New location: $location")
            }.launchIn(viewModelScope)
    }

    var state by mutableStateOf(ActiveRunState())
        private set

    fun onAction(action: ActiveRunAction) {
        when (action) {

            ActiveRunAction.OnFinishRunClick -> {}
            ActiveRunAction.OnResumeClick -> {}
            ActiveRunAction.OnToggleRunClick -> {}
            is ActiveRunAction.ShowLocationPermissionInfo -> {
                _hasLocationPermission.value = action.acceptedLocationPermission
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