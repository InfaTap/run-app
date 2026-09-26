package com.example.run.presentation.run_overview

sealed interface RunOverviewAction {
    data object OnRunClick: RunOverviewAction
    data object OnRunLogoutClick: RunOverviewAction
    data object OnAnalyticsClick: RunOverviewAction
}