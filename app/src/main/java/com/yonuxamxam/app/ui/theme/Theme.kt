package com.yonuxamxam.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = YonuBlue,
    onPrimary = LightSurface,
    primaryContainer = LightSurfaceSubtle,
    onPrimaryContainer = YonuBlueDark,
    secondary = YonuBlueDark,
    onSecondary = LightSurface,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceSecondary,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = YonuBlueLight,
    onPrimary = DarkBackground,
    primaryContainer = DarkSurfaceSubtle,
    onPrimaryContainer = DarkTextPrimary,
    secondary = YonuBlue,
    onSecondary = DarkTextPrimary,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceSecondary,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder
)

/**
 * Accès pratique aux couleurs spécifiques de l'application via [MaterialTheme.yonuColors].
 */
val MaterialTheme.yonuColors: YonuExtraColors
    @Composable
    @ReadOnlyComposable
    get() = LocalYonuColors.current

@Composable
fun YonuXamXamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val yonuColors = if (darkTheme) {
        YonuExtraColors(
            background = DarkBackground,
            surface = DarkSurface,
            surfaceSubtle = DarkSurfaceSubtle,
            surfaceSecondary = DarkSurfaceSecondary,
            textPrimary = DarkTextPrimary,
            textSecondary = DarkTextSecondary,
            border = DarkBorder
        )
    } else {
        YonuExtraColors(
            background = LightBackground,
            surface = LightSurface,
            surfaceSubtle = LightSurfaceSubtle,
            surfaceSecondary = LightSurfaceSecondary,
            textPrimary = LightTextPrimary,
            textSecondary = LightTextSecondary,
            border = LightBorder
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = yonuColors.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalYonuColors provides yonuColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = YonuTypography,
            content = content
        )
    }
}

