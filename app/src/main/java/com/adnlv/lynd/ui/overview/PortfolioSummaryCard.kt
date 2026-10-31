package com.adnlv.lynd.ui.overview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adnlv.lynd.domain.PortfolioSummary
import com.adnlv.lynd.ui.components.MetricItem
import com.adnlv.lynd.ui.theme.LyndTheme
import com.adnlv.lynd.util.Formatters
import java.math.BigDecimal

@Composable
fun PortfolioSummaryCarousel(
    summaries: List<PortfolioSummary>,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(pageCount = { summaries.size }),
) {
    if (summaries.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 20.dp),
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            val summary = summaries[page]
            PortfolioSummaryCard(
                summary = summary,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (summaries.size > 1) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(summaries.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier =
                            Modifier
                                .size(if (isSelected) 8.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) {
                                        LyndTheme.colors.coupon
                                    } else {
                                        LyndTheme.colors.borderSubtle
                                    },
                                ),
                    )
                }
            }
        }
    }
}

@Composable
fun PortfolioSummaryCard(
    summary: PortfolioSummary,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
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
                    .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Portfolio Summary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = LyndTheme.colors.textPrimary,
                )
                Surface(
                    shape = CircleShape,
                    color = LyndTheme.colors.couponContainer,
                    contentColor = LyndTheme.colors.onCouponContainer,
                    border =
                        BorderStroke(
                            width = 1.dp,
                            color = LyndTheme.colors.coupon.copy(alpha = 0.25f),
                        ),
                ) {
                    Text(
                        text = summary.currency,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
            }

            HorizontalDivider(
                color = LyndTheme.colors.borderSubtle.copy(alpha = 0.5f),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                MetricItem(
                    label = "Invested Capital",
                    value = "${Formatters.formatAmount(summary.investedCapital)} ${summary.currency}",
                    modifier = Modifier.weight(1f),
                )
                MetricItem(
                    label = "Expected Payout",
                    value = "${Formatters.formatAmount(summary.expectedPayout)} ${summary.currency}",
                    valueColor = LyndTheme.colors.coupon,
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                val isProfitPositive = summary.totalProfit >= BigDecimal.ZERO
                val profitPrefix = if (isProfitPositive && summary.totalProfit > BigDecimal.ZERO) "+" else ""
                val profitColor =
                    if (isProfitPositive) {
                        LyndTheme.colors.profit
                    } else {
                        LyndTheme.colors.loss
                    }

                MetricItem(
                    label = "Total Profit",
                    value = "$profitPrefix${Formatters.formatAmount(summary.totalProfit)} ${summary.currency}",
                    valueColor = profitColor,
                    modifier = Modifier.weight(1f),
                )
                MetricItem(
                    label = "Avg Interest Rate",
                    value = Formatters.formatPercentage(summary.averageInterestRate),
                    valueColor = LyndTheme.colors.coupon,
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End,
                )
            }
        }
    }
}
