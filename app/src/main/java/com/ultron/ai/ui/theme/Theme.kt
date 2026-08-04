package com.ultron.ai.ui.theme

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun UltronTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    val context = LocalContext.current

    SideEffect {
        val window = (context as? android.app.Activity)?.window
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
            val controller = WindowInsetsControllerCompat(it, view)
            controller.isAppearanceLightStatusBars = false
            controller.isAppearanceLightNavigationBars = false
            it.statusBarColor = Color.Transparent.toArgb()
            it.navigationBarColor = Color.Transparent.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = UltronTypography.typography,
        shapes = UltronShapes.shapes,
        content = content
    )
}

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00D4FF),
    onPrimary = Color(0xFF05070B),
    primaryContainer = Color(0xFF003D4D),
    onPrimaryContainer = Color(0xFF00D4FF),
    secondary = Color(0xFF00FFFF),
    onSecondary = Color(0xFF05070B),
    secondaryContainer = Color(0xFF004D4D),
    onSecondaryContainer = Color(0xFF00FFFF),
    tertiary = Color(0xFFB880FF),
    onTertiary = Color(0xFF05070B),
    tertiaryContainer = Color(0xFF3D204D),
    onTertiaryContainer = Color(0xFFB880FF),
    error = Color(0xFFFF1744),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFF4D0010),
    onErrorContainer = Color(0xFFFF1744),
    background = Color(0xFF05070B),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF0A0D12),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF13161D),
    onSurfaceVariant = Color(0xB3FFFFFF),
    outline = Color(0x33FFFFFF),
    outlineVariant = Color(0x1AFFFFFF),
    shadow = Color(0x00000000),
    scrim = Color(0xCC05070B),
    inverseSurface = Color(0xFFFFFFFF),
    inverseOnSurface = Color(0xFF05070B),
    inversePrimary = Color(0xFF00D4FF),
    surfaceTint = Color(0xFF00D4FF),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006E8A),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD1E8FF),
    onPrimaryContainer = Color(0xFF00202E),
    secondary = Color(0xFF006D6D),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD1E8E8),
    onSecondaryContainer = Color(0xFF002020),
    tertiary = Color(0xFF7A5196),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFEADDFF),
    onTertiaryContainer = Color(0xFF2E1242),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFCFCFF),
    onBackground = Color(0xFF1A1C1E),
    surface = Color(0xFFFCFCFF),
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFDEE3E8),
    onSurfaceVariant = Color(0xFF44474C),
    outline = Color(0xFF74777C),
    outlineVariant = Color(0xFFC4C7CC),
    shadow = Color(0xFF000000),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF2F3033),
    inverseOnSurface = Color(0xFFF2F2F4),
    inversePrimary = Color(0xFF8FD7FF),
    surfaceTint = Color(0xFF006E8A),
)