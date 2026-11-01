package com.example.core.presentation.designsystem.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.presentation.designsystem.AnalyticsIcon
import com.example.core.presentation.designsystem.ArrowLeftIcon
import com.example.core.presentation.designsystem.Poppins
import com.example.core.presentation.designsystem.RunTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunToolbar(
    modifier: Modifier = Modifier,
    showBackButton: Boolean,
    titleString: String,
    menuItems: List<DropDownItem> = emptyList(),
    onMenuItemClick: (index: Int) -> Unit = {},
    onBackButtonClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    startContent: (@Composable () -> Unit)? = null
) {
    var isDropdownOpen by rememberSaveable() {
        mutableStateOf(false)
    }
    Shape
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                startContent?.invoke()
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = titleString,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = Poppins
                )
            }
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackButtonClick) {
                    Icon(
                        imageVector = ArrowLeftIcon, contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        actions = {
            if (menuItems.isNotEmpty()) {
                Box {
                    DropdownMenu(
                        expanded = isDropdownOpen,
                        onDismissRequest = {
                            isDropdownOpen = false
                        }
                    ) {
                        menuItems.forEachIndexed { index, item ->

                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                                    .padding(16.dp)
                                    .clickable {onMenuItemClick(index)}
                                ) {
                                Icon(imageVector = item.icon, contentDescription = "Analytics")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(item.title)
                            }
                        }
                    }
                    IconButton(onClick = { isDropdownOpen = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        },

        )


}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ToolbarPreview() {
    RunTheme {
        RunToolbar(
            showBackButton = false,
            titleString = "Run",
            menuItems = listOf(
                DropDownItem(icon = AnalyticsIcon,
                    title = "Analytics")
            )
        ) { }
    }

}