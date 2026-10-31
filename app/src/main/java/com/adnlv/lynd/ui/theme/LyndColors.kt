package com.adnlv.lynd.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class LyndColors(
    val profit: Color,
    val onProfit: Color,
    val profitContainer: Color,
    val onProfitContainer: Color,
    val loss: Color,
    val onLoss: Color,
    val lossContainer: Color,
    val onLossContainer: Color,
    val coupon: Color,
    val onCoupon: Color,
    val couponContainer: Color,
    val onCouponContainer: Color,
    val principal: Color,
    val onPrincipal: Color,
    val principalContainer: Color,
    val onPrincipalContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val borderSubtle: Color,
    val borderStrong: Color,
    val isDark: Boolean
)

val LyndLightColors = LyndColors(
    profit = LyndProfitGreenLight,
    onProfit = LyndOnProfitLight,
    profitContainer = LyndProfitContainerLight,
    onProfitContainer = LyndOnProfitContainerLight,
    loss = LyndLossRedLight,
    onLoss = LyndOnLossLight,
    lossContainer = LyndLossContainerLight,
    onLossContainer = LyndOnLossContainerLight,
    coupon = LyndBlueLight,
    onCoupon = LyndOnBlueLight,
    couponContainer = LyndBlueContainerLight,
    onCouponContainer = LyndOnBlueContainerLight,
    principal = LyndPrincipalAmberLight,
    onPrincipal = LyndOnPrincipalLight,
    principalContainer = LyndPrincipalContainerLight,
    onPrincipalContainer = LyndOnPrincipalContainerLight,
    warning = LyndWarningYellowLight,
    onWarning = LyndOnWarningLight,
    warningContainer = LyndWarningContainerLight,
    onWarningContainer = LyndOnWarningContainerLight,
    textPrimary = LyndTextPrimaryLight,
    textSecondary = LyndTextSecondaryLight,
    textMuted = LyndTextMutedLight,
    borderSubtle = LyndBorderSubtleLight,
    borderStrong = LyndBorderStrongLight,
    isDark = false
)

val LyndDarkColors = LyndColors(
    profit = LyndProfitGreenDark,
    onProfit = LyndOnProfitDark,
    profitContainer = LyndProfitContainerDark,
    onProfitContainer = LyndOnProfitContainerDark,
    loss = LyndLossRedDark,
    onLoss = LyndOnLossDark,
    lossContainer = LyndLossContainerDark,
    onLossContainer = LyndOnLossContainerDark,
    coupon = LyndBlueDark,
    onCoupon = LyndOnBlueDark,
    couponContainer = LyndBlueContainerDark,
    onCouponContainer = LyndOnBlueContainerDark,
    principal = LyndPrincipalAmberDark,
    onPrincipal = LyndOnPrincipalDark,
    principalContainer = LyndPrincipalContainerDark,
    onPrincipalContainer = LyndOnPrincipalContainerDark,
    warning = LyndWarningYellowDark,
    onWarning = LyndOnWarningDark,
    warningContainer = LyndWarningContainerDark,
    onWarningContainer = LyndOnWarningContainerDark,
    textPrimary = LyndTextPrimaryDark,
    textSecondary = LyndTextSecondaryDark,
    textMuted = LyndTextMutedDark,
    borderSubtle = LyndBorderSubtleDark,
    borderStrong = LyndBorderStrongDark,
    isDark = true
)

val LocalLyndColors = staticCompositionLocalOf<LyndColors> {
    error("No LyndColors provided")
}

object LyndTheme {
    val colors: LyndColors
        @Composable
        @ReadOnlyComposable
        get() = LocalLyndColors.current
}
