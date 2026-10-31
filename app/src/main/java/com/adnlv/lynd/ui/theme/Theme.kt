package com.adnlv.lynd.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

val LyndLightColorScheme = lightColorScheme(
    primary = LyndGoldLight,
    onPrimary = LyndOnGoldLight,
    primaryContainer = LyndGoldContainerLight,
    onPrimaryContainer = LyndOnGoldContainerLight,
    secondary = LyndSecondaryLight,
    onSecondary = LyndOnSecondaryLight,
    secondaryContainer = LyndSecondaryContainerLight,
    onSecondaryContainer = LyndOnSecondaryContainerLight,
    tertiary = LyndProfitGreenLight,
    onTertiary = LyndOnProfitLight,
    tertiaryContainer = LyndProfitContainerLight,
    onTertiaryContainer = LyndOnProfitContainerLight,
    error = LyndLossRedLight,
    onError = LyndOnLossLight,
    errorContainer = LyndLossContainerLight,
    onErrorContainer = LyndOnLossContainerLight,
    background = LyndBackgroundLight,
    onBackground = LyndOnBackgroundLight,
    surface = LyndSurfaceLight,
    onSurface = LyndOnSurfaceLight,
    surfaceVariant = LyndSurfaceVariantLight,
    onSurfaceVariant = LyndOnSurfaceVariantLight,
    surfaceContainer = LyndSurfaceContainerLight,
    surfaceContainerLow = LyndSurfaceContainerLowLight,
    surfaceContainerHighest = LyndSurfaceContainerHighestLight,
    outline = LyndBorderStrongLight,
    outlineVariant = LyndBorderSubtleLight
)

val LyndDarkColorScheme = darkColorScheme(
    primary = LyndGoldDark,
    onPrimary = LyndOnGoldDark,
    primaryContainer = LyndGoldContainerDark,
    onPrimaryContainer = LyndOnGoldContainerDark,
    secondary = LyndSecondaryDark,
    onSecondary = LyndOnSecondaryDark,
    secondaryContainer = LyndSecondaryContainerDark,
    onSecondaryContainer = LyndOnSecondaryContainerDark,
    tertiary = LyndProfitGreenDark,
    onTertiary = LyndOnProfitDark,
    tertiaryContainer = LyndProfitContainerDark,
    onTertiaryContainer = LyndOnProfitContainerDark,
    error = LyndLossRedDark,
    onError = LyndOnLossDark,
    errorContainer = LyndLossContainerDark,
    onErrorContainer = LyndOnLossContainerDark,
    background = LyndBackgroundDark,
    onBackground = LyndOnBackgroundDark,
    surface = LyndSurfaceDark,
    onSurface = LyndOnSurfaceDark,
    surfaceVariant = LyndSurfaceVariantDark,
    onSurfaceVariant = LyndOnSurfaceVariantDark,
    surfaceContainer = LyndSurfaceContainerDark,
    surfaceContainerLow = LyndSurfaceContainerLowDark,
    surfaceContainerHighest = LyndSurfaceContainerHighestDark,
    outline = LyndBorderStrongDark,
    outlineVariant = LyndBorderSubtleDark
)

@Composable
fun LyndTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) LyndDarkColorScheme else LyndLightColorScheme
    val lyndColors = if (darkTheme) LyndDarkColors else LyndLightColors

    CompositionLocalProvider(LocalLyndColors provides lyndColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
