package com.adnlv.lynd.ui.planner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adnlv.lynd.domain.ActionableGap
import com.adnlv.lynd.domain.BondLadderMatch
import com.adnlv.lynd.domain.LadderPaymentType
import com.adnlv.lynd.ui.theme.LyndTheme
import com.adnlv.lynd.util.Formatters
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ActionableGapCard(
    actionableGap: ActionableGap,
    modifier: Modifier = Modifier,
) {
    val gap = actionableGap.gap
    val monthTitle =
        gap.yearMonth
            .format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    val hasCrossCoverage = gap.crossCurrencyCoverage.isNotEmpty()
    val crossCoverageText =
        if (hasCrossCoverage) {
            gap.crossCurrencyCoverage.entries.joinToString(separator = ", ") { (curr, amt) ->
                "$curr ${Formatters.formatAmount(amt)}"
            }
        } else {
            ""
        }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = monthTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = LyndTheme.colors.textPrimary,
                )

                if (gap.totalConsecutiveMonths > 1) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = LyndTheme.colors.warningContainer.copy(alpha = 0.7f),
                    ) {
                        Text(
                            text = "Month ${gap.consecutiveMonthIndex} of ${gap.totalConsecutiveMonths}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = LyndTheme.colors.onWarningContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = LyndTheme.colors.warning,
                        modifier = Modifier.padding(end = 2.dp),
                    )
                    Text(
                        text = "Income decreases to 0 ${gap.currency}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LyndTheme.colors.warning,
                    )
                }

                Text(
                    text = "0.00 ${gap.currency}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = LyndTheme.colors.textSecondary,
                )
            }

            if (hasCrossCoverage) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = LyndTheme.colors.profitContainer.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "🟡 Note: Expected $crossCoverageText equivalent",
                        style = MaterialTheme.typography.bodySmall,
                        color = LyndTheme.colors.onProfitContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    )
                }
            }

            if (actionableGap.recommendedBonds.isNotEmpty()) {
                Text(
                    text = "Recommended Bonds to Fill Gap",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = LyndTheme.colors.textPrimary,
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(actionableGap.recommendedBonds) { match ->
                        BondLadderMatchMiniCard(match = match)
                    }
                }
            }
        }
    }
}

@Composable
fun BondLadderMatchMiniCard(
    match: BondLadderMatch,
    modifier: Modifier = Modifier,
) {
    val bond = match.bond
    val formattedPayDate = match.paymentDate.format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault()))

    val paymentTypeLabel =
        when (match.paymentType) {
            LadderPaymentType.COUPON -> "Coupon"
            LadderPaymentType.REDEMPTION -> "Maturity"
            LadderPaymentType.COUPON_AND_REDEMPTION -> "Coupon + Maturity"
        }

    Card(
        modifier = modifier.width(240.dp),
        shape = RoundedCornerShape(12.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = bond.isin,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = LyndTheme.colors.textPrimary,
                )

                Text(
                    text = Formatters.formatCouponRate(bond.couponRate),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = LyndTheme.colors.coupon,
                )
            }

            Text(
                text = bond.name,
                style = MaterialTheme.typography.bodySmall,
                color = LyndTheme.colors.textSecondary,
                maxLines = 1,
            )

            Text(
                text = "${Formatters.formatAmount(match.paymentAmount)} ${bond.currency} ($paymentTypeLabel)",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = LyndTheme.colors.textPrimary,
            )

            Text(
                text = "Payout: $formattedPayDate",
                style = MaterialTheme.typography.labelSmall,
                color = LyndTheme.colors.textSecondary,
            )

            if (match.totalGapsCovered > 1) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = LyndTheme.colors.couponContainer,
                ) {
                    Text(
                        text = "✨ Fills ${match.totalGapsCovered} dry months!",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = LyndTheme.colors.onCouponContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    )
                }
            }
        }
    }
}
