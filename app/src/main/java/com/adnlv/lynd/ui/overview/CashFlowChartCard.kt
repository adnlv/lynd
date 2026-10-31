package com.adnlv.lynd.ui.overview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adnlv.lynd.domain.MonthlyCashFlow
import com.adnlv.lynd.ui.components.SnapBarChart
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
                        text = "${Formatters.formatAmount(total12MonthsIncome)} $currency",
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
                val maxMonthlyAmount =
                    remember(cashFlows) {
                        val max = cashFlows.maxOfOrNull { it.totalAmount } ?: BigDecimal.ZERO
                        if (max > BigDecimal.ZERO) max else BigDecimal.ONE
                    }

                SnapBarChart(
                    items = cashFlows,
                    selectedIndex = selectedIndex,
                    onSelectedIndexChange = { selectedIndex = it },
                ) { monthFlow, index, isSelected ->
                    val monthLabel =
                        remember(monthFlow.yearMonth) {
                            monthFlow.yearMonth.month
                                .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                                .filter { it.isLetter() }
                                .take(3)
                        }
                    CashFlowBar(
                        monthFlow = monthFlow,
                        monthLabel = monthLabel,
                        maxAmount = maxMonthlyAmount,
                        isSelected = isSelected,
                        couponColor = LyndTheme.colors.chartPrimary,
                        principalColor = LyndTheme.colors.chartSecondary,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                val selectedFlow = cashFlows.getOrNull(selectedIndex)
                if (selectedFlow != null) {
                    CashFlowDetailCard(
                        selectedFlow = selectedFlow,
                        currency = currency,
                    )
                }
            }
        }
    }
}
