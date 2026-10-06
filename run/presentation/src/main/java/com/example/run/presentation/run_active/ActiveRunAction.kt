package com.example.run.presentation.run_active

sealed interface ActiveRunAction {
    data object OnToggleRunClick: ActiveRunAction

    data object OnFinishRunClick: ActiveRunAction

    data object OnResumeClick: ActiveRunAction

    data object OnBackClick: ActiveRunAction

    data class ShowLocationPermissionInfo(
        val acceptedLocationPermission: Boolean,
        val showLocationRationale: Boolean
    ): ActiveRunAction

    data class ShowNotificationPermissionInfo(
        val acceptedNotificationPermission: Boolean,
        val acceptedNotificationRationale: Boolean,
    ): ActiveRunAction

    data object DismissRationalDialog: ActiveRunAction
}