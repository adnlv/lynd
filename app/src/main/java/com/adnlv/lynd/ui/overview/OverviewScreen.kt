package com.adnlv.lynd.ui.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adnlv.lynd.ui.components.LyndNoHoldingsPlate
import com.adnlv.lynd.ui.components.LyndScreenHeader

@Composable
fun OverviewScreen(
    viewModel: OverviewViewModel,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    onNavigateToHoldings: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (uiState.summaries.isEmpty()) {
            LyndScreenHeader(
                title = "Overview",
                modifier = Modifier.padding(top = 8.dp, start = 20.dp, end = 20.dp)
            )
            LyndNoHoldingsPlate(
                title = "No portfolio data",
                description = "Add your bond holdings to see cash flows, balances, and maturity timelines.",
                onAddHolding = onNavigateToHoldings,
                onLoadTestPortfolio = { viewModel.loadTestPortfolio() },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        } else {
            val pagerState = rememberPagerState(pageCount = { uiState.summaries.size })
            val activeSummary = uiState.summaries.getOrNull(pagerState.currentPage)
            val activeCurrency = activeSummary?.currency ?: uiState.summaries.firstOrNull()?.currency ?: "UAH"
            val activeCashFlows = uiState.cashFlowsByCurrency[activeCurrency].orEmpty()
            val activeMaturities = uiState.yearlyMaturitiesByCurrency[activeCurrency].orEmpty()

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item(key = "overview_header") {
                    LyndScreenHeader(title = "Overview")
                }

                item {
                    PortfolioSummaryCarousel(
                        summaries = uiState.summaries,
                        pagerState = pagerState
                    )
                }

                item {
                    CashFlowChartCard(
                        cashFlows = activeCashFlows,
                        currency = activeCurrency,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }

                item {
                    MaturityScheduleChartCard(
                        maturities = activeMaturities,
                        currency = activeCurrency,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        }
    }
}
