package com.yonuxamxam.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Couleurs de marque Yonu Xam Xam
val YonuBlue = Color(0xFF2563EB)
val YonuBlueDark = Color(0xFF1E3A8A)
val YonuBlueLight = Color(0xFF3B82F6)

// Dégradé principal de l'application
val YonuDiagonalGradient = Brush.linearGradient(
    colors = listOf(YonuBlueLight, YonuBlueDark)
)

// Couleurs d'accent et indicateurs
val EmeraldSuccess = Color(0xFF10B981)
val RedNotificationBadge = Color(0xFFEF4444)
val AmberPending = Color(0xFFF59E0B)

// Couleurs Thème Clair
val LightBackground = Color(0xFFF5F7FB)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceSubtle = Color(0xFFEFF6FF)
val LightSurfaceSecondary = Color(0xFFF1F5F9)
val LightTextPrimary = Color(0xFF18181B)
val LightTextSecondary = Color(0xFF71717A)
val LightBorder = Color(0xFFE4E4E7)

// Couleurs Thème Sombre
val DarkBackground = Color(0xFF0F172A)
val DarkSurface = Color(0xFF1E293B)
val DarkSurfaceSubtle = Color(0xFF1E3A8A).copy(alpha = 0.35f)
val DarkSurfaceSecondary = Color(0xFF27354A)
val DarkTextPrimary = Color(0xFFF1F5F9)
val DarkTextSecondary = Color(0xFF94A3B8)
val DarkBorder = Color(0xFF334155)

/**
 * Couleurs sémantiques personnalisées pour garantir une parfaite lisibilité
 * dans le mode clair et le mode sombre selon les exigences du projet.
 */
@Immutable
data class YonuExtraColors(
    val background: Color,
    val surface: Color,
    val surfaceSubtle: Color,
    val surfaceSecondary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val border: Color,
    val yonuBlue: Color = YonuBlue,
    val yonuBlueDark: Color = YonuBlueDark,
    val yonuBlueLight: Color = YonuBlueLight,
    val emeraldSuccess: Color = EmeraldSuccess,
    val badgeRed: Color = RedNotificationBadge
)

val LocalYonuColors = staticCompositionLocalOf {
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

