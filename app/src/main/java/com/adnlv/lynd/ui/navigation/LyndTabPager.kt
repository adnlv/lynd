package com.adnlv.lynd.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.adnlv.lynd.ui.overview.OverviewScreen
import com.adnlv.lynd.ui.overview.OverviewViewModel
import com.adnlv.lynd.ui.payouts.PayoutsScreen
import com.adnlv.lynd.ui.payouts.PayoutsViewModel
import com.adnlv.lynd.ui.planner.PlannerScreen
import com.adnlv.lynd.ui.planner.PlannerViewModel

@Composable
fun LyndTabPager(
    pagerState: PagerState,
    overviewViewModel: OverviewViewModel,
    plannerViewModel: PlannerViewModel,
    payoutsViewModel: PayoutsViewModel,
    overviewListState: LazyListState,
    plannerListState: LazyListState,
    payoutsListState: LazyListState,
    bottomBarActionState: BottomBarActionState?,
    onNavigateToHoldings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val visitedPages = remember { mutableStateListOf(0) }
    LaunchedEffect(pagerState.currentPage) {
        if (!visitedPages.contains(pagerState.currentPage)) {
            visitedPages.add(pagerState.currentPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false,
        beyondViewportPageCount = 2,
        modifier = modifier.fillMaxSize(),
    ) { page ->
        if (visitedPages.contains(page) || page == pagerState.currentPage) {
            when (page) {
                0 ->
                    OverviewScreen(
                        viewModel = overviewViewModel,
                        listState = overviewListState,
                        onNavigateToHoldings = onNavigateToHoldings,
                    )
                1 ->
                    PlannerScreen(
                        viewModel = plannerViewModel,
                        listState = plannerListState,
                        onNavigateToHoldings = onNavigateToHoldings,
                    )
                2 ->
                    PayoutsScreen(
                        viewModel = payoutsViewModel,
                        listState = payoutsListState,
                        onActionChange = { action ->
                            bottomBarActionState?.setAction(Screen.Payouts.route, action)
                        },
                        onNavigateToHoldings = onNavigateToHoldings,
                    )
            }
        } else {
            Box(modifier = Modifier.fillMaxSize())
        }
    }
}
