package com.adnlv.lynd.ui.overview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.adnlv.lynd.domain.YearlyMaturity
import com.adnlv.lynd.ui.components.SmoothAreaChart
import com.adnlv.lynd.ui.theme.LyndTheme
import com.adnlv.lynd.util.Formatters
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun MaturityScheduleChartCard(
    maturities: List<YearlyMaturity>,
    currency: String,
    modifier: Modifier = Modifier,
) {
    val totalPrincipal =
        remember(maturities) {
            maturities.fold(BigDecimal.ZERO) { acc, item -> acc.add(item.amount) }
        }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
        border =
            BorderStroke(
                width = 1.dp,
                color = LyndTheme.colors.borderSubtle.copy(alpha = 0.5f),
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            var selectedIndex by remember(maturities) {
                val firstNonZeroIndex = maturities.indexOfFirst { it.amount > BigDecimal.ZERO }
                mutableStateOf(if (firstNonZeroIndex >= 0) firstNonZeroIndex else 0)
            }

            val currencySymbol = remember(currency) { Formatters.getCurrencySymbol(currency) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Maturity Schedule",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LyndTheme.colors.textPrimary,
                    )
                    Text(
                        text = "Principal redemption by year",
                        style = MaterialTheme.typography.labelSmall,
                        color = LyndTheme.colors.textSecondary,
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total Principal",
                        style = MaterialTheme.typography.labelSmall,
                        color = LyndTheme.colors.textSecondary,
                    )
                    Text(
                        text = "${Formatters.formatAmount(totalPrincipal)} $currencySymbol",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LyndTheme.colors.chartSecondary,
                    )
                }
            }

            if (maturities.isEmpty() || totalPrincipal <= BigDecimal.ZERO) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "No upcoming maturities.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LyndTheme.colors.textSecondary,
                    )
                }
            } else {
                SmoothAreaChart(
                    items = maturities,
                    selectedIndex = selectedIndex,
                    onSelectedIndexChange = { selectedIndex = it },
                    valueSelector = { it.amount.toFloat() },
                    xLabelSelector = { "${it.year}" },
                    tooltipTitleSelector = { "${it.year}" },
                    tooltipValueSelector = { maturity ->
                        "$currencySymbol${Formatters.formatAmount(maturity.amount)}"
                    },
                    currencySymbol = currencySymbol,
                    modifier = Modifier.fillMaxWidth(),
                )

                val selectedMaturity = maturities.getOrNull(selectedIndex) ?: maturities.firstOrNull()

                val percentageOfTotal =
                    remember(selectedMaturity?.amount, totalPrincipal) {
                        if (selectedMaturity != null && totalPrincipal > BigDecimal.ZERO) {
                            selectedMaturity.amount
                                .multiply(BigDecimal(100))
                                .divide(totalPrincipal, 1, RoundingMode.HALF_UP)
                        } else {
                            BigDecimal.ZERO
                        }
                    }

                HorizontalDivider(
                    color = LyndTheme.colors.borderSubtle.copy(alpha = 0.5f),
                    thickness = 1.dp,
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Left: Principal badge
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier =
                                        Modifier
                                            .size(10.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(LyndTheme.colors.chartSecondary),
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Principal",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LyndTheme.colors.textSecondary,
                                    maxLines = 1,
                                )
                            }
                            Text(
                                text = "$currencySymbol${Formatters.formatAmount(selectedMaturity?.amount ?: BigDecimal.ZERO)}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = LyndTheme.colors.textPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                        // Right: Share badge
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Share",
                                style = MaterialTheme.typography.bodySmall,
                                color = LyndTheme.colors.textSecondary,
                                maxLines = 1,
                            )
                            Text(
                                text = Formatters.formatPercentage(percentageOfTotal),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = LyndTheme.colors.textPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }

                    Text(
                        text = "Drag to inspect",
                        style = MaterialTheme.typography.bodySmall,
                        color = LyndTheme.colors.textSecondary,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
