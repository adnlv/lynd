package com.adnlv.lynd.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class LyndColors(
    // Action group
    val actionPrimary: Color,
    val onActionPrimary: Color,
    val actionSecondary: Color,
    val onActionSecondary: Color,
    // Chart group
    val chartPrimary: Color,
    val chartSecondary: Color,
    val chartTrack: Color,
    // Feedback group
    val feedbackContainer: Color,
    val onFeedbackContainer: Color,
    // Unique badge group
    val badgeCouponContainer: Color,
    val onBadgeCouponContainer: Color,
    val badgeRedemptionContainer: Color,
    val onBadgeRedemptionContainer: Color,
    val badgeNeutralContainer: Color,
    val onBadgeNeutralContainer: Color,
    // Financial semantics (preserved for upcoming color assignment)
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
    // Typography & borders
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val borderSubtle: Color,
    val borderStrong: Color,
    val isDark: Boolean,
)

val LyndLightColors =
    LyndColors(
        actionPrimary = LyndActionPrimaryLight,
        onActionPrimary = LyndOnActionPrimaryLight,
        actionSecondary = LyndActionSecondaryLight,
        onActionSecondary = LyndOnActionSecondaryLight,
        chartPrimary = LyndChartPrimaryLight,
        chartSecondary = LyndChartSecondaryLight,
        chartTrack = LyndChartTrackLight,
        feedbackContainer = LyndFeedbackContainerLight,
        onFeedbackContainer = LyndOnFeedbackContainerLight,
        badgeCouponContainer = LyndBadgeCouponContainerLight,
        onBadgeCouponContainer = LyndOnBadgeCouponContainerLight,
        badgeRedemptionContainer = LyndBadgeRedemptionContainerLight,
        onBadgeRedemptionContainer = LyndOnBadgeRedemptionContainerLight,
        badgeNeutralContainer = LyndBadgeNeutralContainerLight,
        onBadgeNeutralContainer = LyndOnBadgeNeutralContainerLight,
        profit = LyndProfitGreenLight,
        onProfit = LyndOnProfitLight,
        profitContainer = LyndProfitContainerLight,
        onProfitContainer = LyndOnProfitContainerLight,
        loss = LyndLossTerracottaLight,
        onLoss = LyndOnLossLight,
        lossContainer = LyndLossContainerLight,
        onLossContainer = LyndOnLossContainerLight,
        coupon = LyndMintGreenLight,
        onCoupon = LyndOnMintLight,
        couponContainer = LyndMintContainerLight,
        onCouponContainer = LyndOnMintContainerLight,
        principal = LyndPrincipalSlateLight,
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
        isDark = false,
    )

val LyndDarkColors =
    LyndColors(
        actionPrimary = LyndActionPrimaryDark,
        onActionPrimary = LyndOnActionPrimaryDark,
        actionSecondary = LyndActionSecondaryDark,
        onActionSecondary = LyndOnActionSecondaryDark,
        chartPrimary = LyndChartPrimaryDark,
        chartSecondary = LyndChartSecondaryDark,
        chartTrack = LyndChartTrackDark,
        feedbackContainer = LyndFeedbackContainerDark,
        onFeedbackContainer = LyndOnFeedbackContainerDark,
        badgeCouponContainer = LyndBadgeCouponContainerDark,
        onBadgeCouponContainer = LyndOnBadgeCouponContainerDark,
        badgeRedemptionContainer = LyndBadgeRedemptionContainerDark,
        onBadgeRedemptionContainer = LyndOnBadgeRedemptionContainerDark,
        badgeNeutralContainer = LyndBadgeNeutralContainerDark,
        onBadgeNeutralContainer = LyndOnBadgeNeutralContainerDark,
        profit = LyndProfitGreenDark,
        onProfit = LyndOnProfitDark,
        profitContainer = LyndProfitContainerDark,
        onProfitContainer = LyndOnProfitContainerDark,
        loss = LyndLossTerracottaDark,
        onLoss = LyndOnLossDark,
        lossContainer = LyndLossContainerDark,
        onLossContainer = LyndOnLossContainerDark,
        coupon = LyndMintGreenDark,
        onCoupon = LyndOnMintDark,
        couponContainer = LyndMintContainerDark,
        onCouponContainer = LyndOnMintContainerDark,
        principal = LyndPrincipalSlateDark,
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
        isDark = true,
    )

val LocalLyndColors =
    staticCompositionLocalOf<LyndColors> {
        error("No LyndColors provided")
    }

object LyndTheme {
    val colors: LyndColors
        @Composable
        @ReadOnlyComposable
        get() = LocalLyndColors.current
}
