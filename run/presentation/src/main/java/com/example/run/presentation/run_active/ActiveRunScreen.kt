package com.example.run.presentation.run_active

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.presentation.designsystem.RunIcon
import com.example.core.presentation.designsystem.RunTheme
import com.example.core.presentation.designsystem.StartIcon
import com.example.core.presentation.designsystem.StopIcon
import com.example.core.presentation.designsystem.components.RunFloatingActionButton
import com.example.core.presentation.designsystem.components.RunScaffold
import com.example.core.presentation.designsystem.components.RunToolbar
import com.example.run.presentation.run_active.component.RunDataCard
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
    }) {paddingValues ->

        Box(modifier = Modifier.fillMaxSize().
        background(MaterialTheme.colorScheme.surface)
            ) {
            RunDataCard(elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
                    .padding(paddingValues))
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