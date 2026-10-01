package com.example.run.presentation.run_active

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class ActiveRunViewModel: ViewModel() {

    private val eventChannel = Channel<ActiveRunEvent>()
    val events = eventChannel.receiveAsFlow()



    var state by mutableStateOf(ActiveRunState())
        private set

    fun onAction(action: ActiveRunAction){

    }
}