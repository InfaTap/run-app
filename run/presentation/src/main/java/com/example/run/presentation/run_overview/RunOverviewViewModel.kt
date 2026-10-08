package com.example.run.presentation.run_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.run.RunRepository
import com.example.run.presentation.run_overview.mappers.toRunUi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RunOverviewViewModel(private val runRepository: RunRepository) : ViewModel() {

    var state by mutableStateOf(RunOverviewState())
        private set

    init {
        runRepository.getRuns().onEach { runs ->
            val runUi = runs.map {
                it.toRunUi()
            }
            state = state.copy(runUi)

        }.launchIn(viewModelScope)
        viewModelScope.launch {
            runRepository.fetchRuns()
        }
    }

    fun onAction(action: RunOverviewAction) {
        when(action) {
            RunOverviewAction.OnAnalyticsClick -> Unit
            is RunOverviewAction.OnDeleteClick -> {
                viewModelScope.launch {  runRepository.deleteRun(id = action.run.id)}
            }
            RunOverviewAction.OnRunClick -> Unit
            RunOverviewAction.OnRunLogoutClick -> Unit
        }

    }
}