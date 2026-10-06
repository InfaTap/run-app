package com.example.run.presentation.run_active

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class ActiveRunViewModel: ViewModel() {

    private val eventChannel = Channel<ActiveRunEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _hasLocationPermission = MutableStateFlow(false)



    var state by mutableStateOf(ActiveRunState())
        private set

    fun onAction(action: ActiveRunAction){
        when(action){

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