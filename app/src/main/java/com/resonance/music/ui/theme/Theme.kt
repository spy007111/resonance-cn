package com.resonance.music.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun ResonanceTheme(
    themeRepository: ThemeRepository? = null,
    content: @Composable () -> Unit
) {
    val appTheme by (themeRepository?.currentTheme?.collectAsState(initial = AppTheme.NEON_PULSE)
        ?: androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(AppTheme.NEON_PULSE) })

    val context = LocalContext.current

    // White-first UI: every theme is a white surface with an accent colour.
    // Material You keeps the wallpaper palette but stays light as well.
    val colorScheme = when (appTheme) {
        AppTheme.MATERIAL_YOU -> dynamicLightColorScheme(context)
        else -> appTheme.lightColorScheme()
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Dark (black) status/navigation icons on the white chrome.
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ResonanceTypography,
        content = content
    )
}
