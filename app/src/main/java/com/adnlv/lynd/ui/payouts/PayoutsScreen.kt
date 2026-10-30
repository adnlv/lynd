package com.adnlv.lynd.ui.payouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adnlv.lynd.ui.components.LyndNoHoldingsPlate
import com.adnlv.lynd.ui.components.LyndScreenHeader
import com.adnlv.lynd.ui.icons.FilterList
import com.adnlv.lynd.ui.icons.MaterialSymbols
import com.adnlv.lynd.ui.navigation.BottomBarAction

@Composable
fun PayoutsScreen(
    viewModel: PayoutsViewModel,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    onActionChange: ((BottomBarAction) -> Unit)? = null,
    onNavigateToHoldings: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(
        uiState.selectedTab,
        uiState.availableCurrencies,
        uiState.selectedCurrencies,
        uiState.availablePayoutTypes,
        uiState.selectedPayoutTypes
    ) {
        onActionChange?.invoke(
            BottomBarAction(
                icon = MaterialSymbols.FilterList,
                contentDescription = "Open filter menu",
                activeContentDescription = "Close filter menu",
                menuContent = {
                    PayoutFilterMenuContent(
                        selectedTab = uiState.selectedTab,
                        availableCurrencies = uiState.availableCurrencies,
                        selectedCurrencies = uiState.selectedCurrencies,
                        availablePayoutTypes = uiState.availablePayoutTypes,
                        selectedPayoutTypes = uiState.selectedPayoutTypes,
                        onSelectTab = viewModel::selectTab,
                        onSelectCurrency = viewModel::selectCurrency,
                        onToggleCurrency = viewModel::toggleCurrency,
                        onSelectPayoutType = viewModel::selectPayoutType,
                        onTogglePayoutType = viewModel::togglePayoutType
                    )
                }
            )
        )
    }

    val headingText = when (uiState.selectedTab) {
        PayoutTab.UPCOMING -> "Upcoming Payouts"
        PayoutTab.RECEIVED -> "Received Payouts"
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (uiState.dateGroups.isEmpty()) {
                LyndScreenHeader(title = headingText)
                if (uiState.isPortfolioEmpty) {
                    LyndNoHoldingsPlate(
                        title = "No scheduled payouts",
                        description = "Add bond holdings to track upcoming coupon payments and bond redemptions.",
                        onAddHolding = onNavigateToHoldings,
                        onLoadTestPortfolio = { viewModel.loadTestPortfolio() },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        HoldingsSuggestionCard(
                            onClick = onNavigateToHoldings
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            val emptyMessage = when (uiState.selectedTab) {
                                PayoutTab.UPCOMING -> "No upcoming payouts found."
                                PayoutTab.RECEIVED -> "No received payouts found."
                            }
                            Text(
                                text = emptyMessage,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item(key = "payouts_header") {
                        LyndScreenHeader(
                            title = headingText,
                            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                        )
                    }

                    item(key = "holdings_suggestion") {
                        HoldingsSuggestionCard(
                            onClick = onNavigateToHoldings,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    items(
                        items = uiState.dateGroups,
                        key = { it.date.toString() }
                    ) { group ->
                        PayoutCard(
                            group = group,
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }
}
