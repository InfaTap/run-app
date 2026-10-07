package com.example.core.presentation.designsystem

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val DarkColorScheme = darkColorScheme(
    primary = RunGreen,
    onPrimary = RunBlack,
    secondary = RunWhite,
    onSecondary = RunWhite,
    tertiary = RunWhite,
    primaryContainer = RunGreen30,
    background = RunBlack,
    onBackground = RunWhite,
    surface = RunDarkGray,
    onSurface = RunWhite,
    error = RunDarkRed,
    onError = RunWhite,
    onSurfaceVariant = RunGray,
    errorContainer = RunDarkRed5
)

@Composable
fun RunTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}