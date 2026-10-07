package com.example.run.presentation.run_active

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.presentation.designsystem.RunIcon
import com.example.core.presentation.designsystem.RunTheme
import com.example.core.presentation.designsystem.StartIcon
import com.example.core.presentation.designsystem.StopIcon
import com.example.core.presentation.designsystem.components.RunActionButton
import com.example.core.presentation.designsystem.components.RunActionOutlinedButton
import com.example.core.presentation.designsystem.components.RunAppDialog
import com.example.core.presentation.designsystem.components.RunFloatingActionButton
import com.example.core.presentation.designsystem.components.RunScaffold
import com.example.core.presentation.designsystem.components.RunToolbar
import com.example.run.presentation.maps.TrackerMap
import com.example.run.presentation.run_active.component.RunDataCard
import com.example.run.presentation.util.hasLocationPermission
import com.example.run.presentation.util.hasPostNotification
import com.example.run.presentation.util.shouldShowLocationPermissionRational
import com.example.run.presentation.util.shouldShowPostNotificationPermissionRational
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActiveRunScreenRoot(
    viewModel: ActiveRunViewModel = koinViewModel()
) {
    ActiveRunScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActiveRunScreen(
    state: ActiveRunState,
    onAction: (ActiveRunAction) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val hasCoarsePermission = perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val hasFinePermission = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasPostNotification = if (Build.VERSION.SDK_INT > 33) {
            perms[Manifest.permission.POST_NOTIFICATIONS] == true
        } else true

        val activity = context as ComponentActivity
        val showLocationRational = activity.shouldShowLocationPermissionRational()
        val showPostNotification = activity.shouldShowPostNotificationPermissionRational()

        onAction(
            ActiveRunAction.ShowNotificationPermissionInfo(
                acceptedNotificationPermission = hasPostNotification,
                acceptedNotificationRationale = showPostNotification
            )
        )
        onAction(
            ActiveRunAction.ShowLocationPermissionInfo(
                acceptedLocationPermission = hasFinePermission && hasCoarsePermission,
                showLocationRationale = showLocationRational
            )
        )


    }

    LaunchedEffect(key1 = true) {
        val activity = context as ComponentActivity
        val showLocationRational = activity.shouldShowLocationPermissionRational()
        val showNotificationRational = activity.shouldShowPostNotificationPermissionRational()

        onAction(
            ActiveRunAction.ShowLocationPermissionInfo(
                acceptedLocationPermission = context.hasLocationPermission(),
                showLocationRationale = showLocationRational
            )
        )
        onAction(
            ActiveRunAction.ShowNotificationPermissionInfo(
                acceptedNotificationPermission = context.hasPostNotification(),
                acceptedNotificationRationale = showNotificationRational

            )
        )

        if (!showNotificationRational && !showLocationRational) {
            permissionLauncher.requestRunAppPermission(context)
        }
    }

    RunScaffold(withGradient = false, topBar = {
        RunToolbar(
            showBackButton = true,
            titleString = "Active Run",
            onBackButtonClick = {
                onAction(ActiveRunAction.OnBackClick)
            },

            )

    }, floatingAction = {
        RunFloatingActionButton(

            onClick = { onAction(ActiveRunAction.OnToggleRunClick) },
            icon = if (state.shouldTrack) {
                StopIcon
            } else {
                StartIcon
            },
            iconSize = 20.dp,
            contentDescription = ""
        )
    }) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TrackerMap(
                modifier = Modifier.fillMaxSize(),
                isRunFinished = state.isRunFinished,
                currentLocation = state.location,
                locations = state.runData.locations,
                onSnapshot = {}
            )
            RunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(paddingValues)
            )
        }
    }
    if (!state.shouldTrack && state.hasStartedRunning) {
        RunAppDialog(
            title = "Running is paused",
            onDismiss = {
                onAction(ActiveRunAction.OnResumeClick)
            },
            description = "do you want to resume or finish the workout",
            primaryButton = {
                RunActionButton(
                    text = "Resume",
                    isLoading = false,
                    modifier = Modifier.weight(1f)
                ) {
                    onAction(ActiveRunAction.OnResumeClick)
                }
            }, secondaryButton = {
                RunActionOutlinedButton(
                    text = "Finish",
                    enabled = true,
                    loading = state.isSavingRun,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onAction(ActiveRunAction.OnFinishRunClick)
                    }
                )
            }
        )
    }
    if (state.showLocationRationale || state.showNotificationRationale) {
        RunAppDialog(
            title = "Running is paused",
            onDismiss = {},
            description = when {
                state.showLocationRationale && state.showNotificationRationale -> {
                    "Need location"
                }

                state.showLocationRationale -> {
                    "Need location permission"
                }

                else -> {
                    "In order to "
                }
            },
            primaryButton = {
                RunActionOutlinedButton(
                    text = "Okay",
                    enabled = true,
                    onClick = {
                        onAction(ActiveRunAction.DismissRationalDialog)
                        permissionLauncher.requestRunAppPermission(context)
                    }
                )
            }
        )
    }
}

private fun ActivityResultLauncher<Array<String>>.requestRunAppPermission(
    context: Context
) {

    val hasLocationPermission = context.hasLocationPermission()
    val hasNotificationPermission = context.hasPostNotification()

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val notificationPermissions = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        arrayOf()
    }

    when {
        !hasNotificationPermission && !hasLocationPermission -> {
            launch(locationPermissions + notificationPermissions)
        }

        !hasNotificationPermission -> {
            launch(notificationPermissions)
        }

        !hasLocationPermission -> {
            launch(locationPermissions)
        }
    }
}

@Preview
@Composable
private fun ActiveRunScreenPreview() {
    RunTheme {
        ActiveRunScreen(
            state = ActiveRunState(),
            onAction = {}
        )
    }
}