package de.dbaelz.pnp.logbook.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

private val lightScheme = Colors(
    primary = primaryLight,
    primaryVariant = primaryVariantLight,
    onPrimary = onPrimaryLight,
    secondary = secondaryLight,
    secondaryVariant = secondaryVariantLight,
    onSecondary = onSecondaryLight,
    error = errorLight,
    onError = onErrorLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    isLight = true
)

private val darkScheme = Colors(
    primary = primaryDark,
    primaryVariant = primaryVariantDark,
    onPrimary = onPrimaryDark,
    secondary = secondaryDark,
    secondaryVariant = secondaryVariantDark,
    onSecondary = onSecondaryDark,
    error = errorDark,
    onError = onErrorDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    isLight = false
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colorScheme = when {
        isSystemInDarkTheme() -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colors = colorScheme,
        content = content
    )
}


