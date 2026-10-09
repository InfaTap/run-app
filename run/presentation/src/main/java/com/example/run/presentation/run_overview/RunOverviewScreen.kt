package com.example.run.presentation.run_overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.presentation.designsystem.AnalyticsIcon
import com.example.core.presentation.designsystem.LogoIcon
import com.example.core.presentation.designsystem.LogoutIcon
import com.example.core.presentation.designsystem.RunGreen
import com.example.core.presentation.designsystem.RunIcon
import com.example.core.presentation.designsystem.RunTheme
import com.example.core.presentation.designsystem.components.DropDownItem
import com.example.core.presentation.designsystem.components.RunFloatingActionButton
import com.example.core.presentation.designsystem.components.RunScaffold
import com.example.core.presentation.designsystem.components.RunToolbar
import com.example.run.presentation.run_overview.components.RunListItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunOverViewScreenRoot(
    onRunClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: RunOverviewViewModel = koinViewModel()
) {
    /*These two lines do the same thing:

    RunOverViewScreen(onAction = viewModel::onAction)
    RunOverViewScreen(onAction = { action -> viewModel.onAction(action) })*/

    RunOverViewScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {

                RunOverviewAction.OnRunClick -> onRunClick()
                RunOverviewAction.OnRunLogoutClick -> onLogoutClick()
                else -> {}
            }
            viewModel.onAction(action)
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RunOverViewScreen(
    state: RunOverviewState,
    onAction: (RunOverviewAction) -> Unit
) {

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState
    )

    RunScaffold(
        topBar = {
            RunToolbar(
                showBackButton = false,
                titleString = "Run App",
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = scrollBehavior,
                onMenuItemClick = { index ->
                    when (index) {
                        0 -> {
                            onAction(RunOverviewAction.OnAnalyticsClick)
                        }

                        1 -> {
                            onAction(RunOverviewAction.OnRunLogoutClick)
                        }
                    }
                },
                menuItems = listOf(
                    DropDownItem(
                        icon = AnalyticsIcon,
                        title = "Analytics"
                    ),
                    DropDownItem(
                        icon = LogoutIcon,
                        title = "Logout"
                    )
                ),
                startContent = {
                    Icon(
                        imageVector = LogoIcon,
                        "",
                        tint = RunGreen,
                        modifier = Modifier.size(35.dp)
                    )
                }
            )
        },
        floatingAction = {
            RunFloatingActionButton(
                onClick = {
                    onAction(RunOverviewAction.OnRunClick)
                },
                icon = RunIcon,
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(16.dp),
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            items(items = state.list, key = { it.id }) {

                RunListItem(
                    runUi = it,
                    onDeleteClick = {
                        onAction(RunOverviewAction.OnDeleteClick(it))
                    }
                )

            }
        }

    }


}

@Preview
@Composable
private fun RunOverViewScreenPreview() {
    RunTheme {
        RunOverViewScreen(
            state = RunOverviewState(),

            onAction = {}
        )
    }
}