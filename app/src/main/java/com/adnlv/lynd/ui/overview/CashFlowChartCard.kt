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
import com.adnlv.lynd.domain.MonthlyCashFlow
import com.adnlv.lynd.ui.components.SmoothAreaChart
import com.adnlv.lynd.ui.theme.LyndTheme
import com.adnlv.lynd.util.Formatters
import java.math.BigDecimal
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CashFlowChartCard(
    cashFlows: List<MonthlyCashFlow>,
    currency: String,
    modifier: Modifier = Modifier,
) {
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
            val total12MonthsIncome =
                remember(cashFlows) {
                    cashFlows.fold(BigDecimal.ZERO) { acc, item -> acc.add(item.totalAmount) }
                }

            var selectedIndex by remember(cashFlows) {
                val firstNonZeroIndex = cashFlows.indexOfFirst { it.totalAmount > BigDecimal.ZERO }
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
                        text = "Monthly Cash Flow",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LyndTheme.colors.textPrimary,
                    )
                    Text(
                        text = "Next 12 months",
                        style = MaterialTheme.typography.labelSmall,
                        color = LyndTheme.colors.textSecondary,
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total Upcoming",
                        style = MaterialTheme.typography.labelSmall,
                        color = LyndTheme.colors.textSecondary,
                    )
                    Text(
                        text = "${Formatters.formatAmount(total12MonthsIncome)} $currencySymbol",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LyndTheme.colors.chartPrimary,
                    )
                }
            }

            if (cashFlows.isEmpty() || total12MonthsIncome <= BigDecimal.ZERO) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "No upcoming payments.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LyndTheme.colors.textSecondary,
                    )
                }
            } else {
                SmoothAreaChart(
                    items = cashFlows,
                    selectedIndex = selectedIndex,
                    onSelectedIndexChange = { selectedIndex = it },
                    valueSelector = { it.totalAmount.toFloat() },
                    xLabelSelector = { flow ->
                        flow.yearMonth.month
                            .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                            .filter { it.isLetter() }
                            .take(3)
                    },
                    tooltipTitleSelector = { flow ->
                        val monthName = flow.yearMonth.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                        "$monthName ${flow.yearMonth.year}"
                    },
                    tooltipValueSelector = { flow ->
                        "$currencySymbol${Formatters.formatAmount(flow.totalAmount)}"
                    },
                    currencySymbol = currencySymbol,
                    modifier = Modifier.fillMaxWidth(),
                )

                val selectedFlow = cashFlows.getOrNull(selectedIndex) ?: cashFlows.firstOrNull()

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
                        // Left: Coupon badge
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
                                            .background(LyndTheme.colors.chartPrimary),
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Coupons",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LyndTheme.colors.textSecondary,
                                    maxLines = 1,
                                )
                            }
                            Text(
                                text = "$currencySymbol${Formatters.formatAmount(selectedFlow?.couponAmount ?: BigDecimal.ZERO)}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = LyndTheme.colors.textPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                        // Right: Principal badge
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
                                text = "$currencySymbol${Formatters.formatAmount(selectedFlow?.principalAmount ?: BigDecimal.ZERO)}",
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
