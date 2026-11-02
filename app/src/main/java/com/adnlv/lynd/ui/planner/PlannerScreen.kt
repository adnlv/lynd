package com.adnlv.lynd.ui.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adnlv.lynd.ui.components.LyndNoHoldingsPlate
import com.adnlv.lynd.ui.components.LyndScreenHeader

@Composable
fun PlannerScreen(
    viewModel: PlannerViewModel,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    onNavigateToHoldings: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        if (uiState.summaries.isEmpty()) {
            LyndScreenHeader(
                title = "Income Gap Detector",
                modifier = Modifier.padding(top = 8.dp, start = 20.dp, end = 20.dp),
            )
            LyndNoHoldingsPlate(
                title = "No income to plan",
                description = "Add bond holdings to identify months without coupon payments and plan cash flow.",
                onAddHolding = onNavigateToHoldings,
                onLoadTestPortfolio = { viewModel.loadTestPortfolio() },
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
            )
        } else {
            IncomeGapGroup(
                uiState = uiState,
                onHorizonSelected = { viewModel.setPlannerHorizon(it) },
                onCurrencySelected = { viewModel.setPlannerCurrency(it) },
                listState = listState,
            )
        }
    }
}
