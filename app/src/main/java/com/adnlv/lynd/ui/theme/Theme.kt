package com.adnlv.lynd.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

val LyndLightColorScheme =
    lightColorScheme(
        primary = LyndPrimarySlateLight,
        onPrimary = LyndOnPrimaryLight,
        primaryContainer = LyndPrimaryContainerLight,
        onPrimaryContainer = LyndOnPrimaryContainerLight,
        secondary = LyndSecondaryLight,
        onSecondary = LyndOnSecondaryLight,
        secondaryContainer = LyndSecondaryContainerLight,
        onSecondaryContainer = LyndOnSecondaryContainerLight,
        tertiary = LyndMintGreenLight,
        onTertiary = LyndOnMintLight,
        tertiaryContainer = LyndMintContainerLight,
        onTertiaryContainer = LyndOnMintContainerLight,
        error = LyndErrorCrimsonLight,
        onError = LyndOnErrorLight,
        errorContainer = LyndErrorContainerLight,
        onErrorContainer = LyndOnErrorContainerLight,
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
        outlineVariant = LyndBorderSubtleLight,
    )

val LyndDarkColorScheme =
    darkColorScheme(
        primary = LyndPrimarySlateDark,
        onPrimary = LyndOnPrimaryDark,
        primaryContainer = LyndPrimaryContainerDark,
        onPrimaryContainer = LyndOnPrimaryContainerDark,
        secondary = LyndSecondaryDark,
        onSecondary = LyndOnSecondaryDark,
        secondaryContainer = LyndSecondaryContainerDark,
        onSecondaryContainer = LyndOnSecondaryContainerDark,
        tertiary = LyndMintGreenDark,
        onTertiary = LyndOnMintDark,
        tertiaryContainer = LyndMintContainerDark,
        onTertiaryContainer = LyndOnMintContainerDark,
        error = LyndErrorCrimsonDark,
        onError = LyndOnErrorDark,
        errorContainer = LyndErrorContainerDark,
        onErrorContainer = LyndOnErrorContainerDark,
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
        outlineVariant = LyndBorderSubtleDark,
    )

@Composable
fun LyndTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) LyndDarkColorScheme else LyndLightColorScheme
    val lyndColors = if (darkTheme) LyndDarkColors else LyndLightColors

    CompositionLocalProvider(LocalLyndColors provides lyndColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}
