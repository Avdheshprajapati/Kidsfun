package com.ultron.ai.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = ColorScheme(
    primary = Color(0xFF00D4FF),        // Neon Blue
    onPrimary = Color(0xFF05070B),      // AMOLED Black
    primaryContainer = Color(0xFF003D4D),
    onPrimaryContainer = Color(0xFF00D4FF),
    secondary = Color(0xFF00FFFF),      // Electric Cyan
    onSecondary = Color(0xFF05070B),
    secondaryContainer = Color(0xFF004D4D),
    onSecondaryContainer = Color(0xFF00FFFF),
    tertiary = Color(0xFFB880FF),       // Soft Purple
    onTertiary = Color(0xFF05070B),
    tertiaryContainer = Color(0xFF3D204D),
    onTertiaryContainer = Color(0xFFB880FF),
    error = Color(0xFFFF1744),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFF4D0010),
    onErrorContainer = Color(0xFFFF1744),
    background = Color(0xFF05070B),     // AMOLED Black
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF0A0D12),        // AMOLED Black Surface
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF13161D), // Card Background
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

@Composable
fun UltronColorScheme(): ColorScheme = DarkColorScheme