package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = SpendWisePrimary,
    onPrimary = SpendWiseOnPrimary,
    primaryContainer = SpendWisePrimaryContainer,
    onPrimaryContainer = SpendWiseOnPrimaryContainer,
    secondary = SpendWiseSecondary,
    onSecondary = SpendWiseOnSecondary,
    secondaryContainer = SpendWiseSecondaryContainer,
    onSecondaryContainer = SpendWiseOnSecondaryContainer,
    background = SpendWiseBackground,
    onBackground = SpendWiseOnBackground,
    surface = SpendWiseSurface,
    onSurface = SpendWiseOnSurface,
    surfaceVariant = SpendWiseSurfaceVariant,
    onSurfaceVariant = SpendWiseOnSurfaceVariant,
    surfaceContainerLowest = SpendWiseSurfaceContainerLowest,
    surfaceContainerLow = SpendWiseSurfaceContainerLow,
    surfaceContainer = SpendWiseSurfaceContainer,
    surfaceContainerHigh = SpendWiseSurfaceContainerHigh,
    surfaceContainerHighest = SpendWiseSurfaceContainerHighest,
    outline = SpendWiseOutline,
    outlineVariant = SpendWiseOutlineVariant,
    error = SpendWiseError,
    onError = SpendWiseOnError,
    errorContainer = SpendWiseErrorContainer,
    onErrorContainer = SpendWiseOnErrorContainer
)

private val DarkColorScheme = darkColorScheme(
    primary = SpendWisePrimary,
    onPrimary = SpendWiseOnPrimary,
    primaryContainer = SpendWisePrimaryContainer,
    onPrimaryContainer = SpendWiseOnPrimaryContainer,
    secondary = SpendWiseSecondaryFixedDim,
    onSecondary = SpendWiseOnSecondary,
    secondaryContainer = SpendWiseSecondaryContainer,
    onSecondaryContainer = SpendWiseOnSecondaryContainer,
    background = SpendWiseBackground,
    onBackground = SpendWiseOnBackground,
    surface = SpendWiseSurface,
    onSurface = SpendWiseOnSurface,
    surfaceVariant = SpendWiseSurfaceVariant,
    onSurfaceVariant = SpendWiseOnSurfaceVariant,
    surfaceContainerLowest = SpendWiseSurfaceContainerLowest,
    surfaceContainerLow = SpendWiseSurfaceContainerLow,
    surfaceContainer = SpendWiseSurfaceContainer,
    surfaceContainerHigh = SpendWiseSurfaceContainerHigh,
    surfaceContainerHighest = SpendWiseSurfaceContainerHighest,
    outline = SpendWiseOutline,
    outlineVariant = SpendWiseOutlineVariant,
    error = SpendWiseError,
    onError = SpendWiseOnError,
    errorContainer = SpendWiseErrorContainer,
    onErrorContainer = SpendWiseOnErrorContainer
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep SpendWise custom slate & emerald fintech identity consistent
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
