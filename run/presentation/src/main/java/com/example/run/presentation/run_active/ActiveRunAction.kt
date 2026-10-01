package com.example.run.presentation.run_active

sealed interface ActiveRunAction {
    data object OnToggleRunClick: ActiveRunAction

    data object OnFinishRunClick: ActiveRunAction

    data object OnResumeClick: ActiveRunAction

    data object OnBackClick: ActiveRunAction
}