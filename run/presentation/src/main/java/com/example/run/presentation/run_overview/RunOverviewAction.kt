package com.example.run.presentation.run_overview

import com.example.run.presentation.run_overview.model.RunUi

sealed interface RunOverviewAction {
    data object OnRunClick: RunOverviewAction
    data object OnRunLogoutClick: RunOverviewAction
    data object OnAnalyticsClick: RunOverviewAction
    data class OnDeleteClick(val run: RunUi): RunOverviewAction
}