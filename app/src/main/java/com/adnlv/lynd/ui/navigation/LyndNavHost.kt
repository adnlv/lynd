package com.adnlv.lynd.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.adnlv.lynd.AppContainer
import com.adnlv.lynd.ui.holdings.HoldingsScreen
import com.adnlv.lynd.ui.holdings.HoldingsViewModel
import com.adnlv.lynd.ui.overview.OverviewScreen
import com.adnlv.lynd.ui.overview.OverviewViewModel
import com.adnlv.lynd.ui.payouts.PayoutsScreen
import com.adnlv.lynd.ui.payouts.PayoutsViewModel
import com.adnlv.lynd.ui.planner.PlannerScreen
import com.adnlv.lynd.ui.planner.PlannerViewModel
import com.adnlv.lynd.ui.theme.LyndLayoutDefaults
import kotlinx.coroutines.launch

@Composable
fun LyndNavHost(
    navController: NavHostController,
    appContainer: AppContainer,
    modifier: Modifier = Modifier,
    bottomBarActionState: BottomBarActionState? = null
) {
    val activity = LocalContext.current as ComponentActivity

    val overviewViewModel: OverviewViewModel = viewModel(
        viewModelStoreOwner = activity,
        factory = OverviewViewModel.provideFactory(
            holdingDao = appContainer.database.holdingDao(),
            bondDao = appContainer.database.bondDao(),
            payoutDao = appContainer.database.payoutDao()
        )
    )
    val plannerViewModel: PlannerViewModel = viewModel(
        viewModelStoreOwner = activity,
        factory = PlannerViewModel.provideFactory(
            holdingDao = appContainer.database.holdingDao(),
            bondDao = appContainer.database.bondDao(),
            payoutDao = appContainer.database.payoutDao()
        )
    )
    val payoutsViewModel: PayoutsViewModel = viewModel(
        viewModelStoreOwner = activity,
        factory = PayoutsViewModel.provideFactory(
            payoutDao = appContainer.database.payoutDao(),
            holdingDao = appContainer.database.holdingDao(),
            bondDao = appContainer.database.bondDao()
        )
    )
    val holdingsViewModel: HoldingsViewModel = viewModel(
        viewModelStoreOwner = activity,
        factory = HoldingsViewModel.provideFactory(
            holdingDao = appContainer.database.holdingDao(),
            nbuRepository = appContainer.nbuRepository,
            bondDao = appContainer.database.bondDao()
        )
    )

    val overviewListState = rememberLazyListState()
    val plannerListState = rememberLazyListState()
    val payoutsListState = rememberLazyListState()
    val holdingsListState = rememberLazyListState()

    RegisterScrollToTopHandler(
        route = Screen.Overview.route,
        bottomBarActionState = bottomBarActionState,
        listState = overviewListState
    )
    RegisterScrollToTopHandler(
        route = Screen.Planner.route,
        bottomBarActionState = bottomBarActionState,
        listState = plannerListState
    )
    RegisterScrollToTopHandler(
        route = Screen.Payouts.route,
        bottomBarActionState = bottomBarActionState,
        listState = payoutsListState
    )
    RegisterScrollToTopHandler(
        route = Screen.Holdings.route,
        bottomBarActionState = bottomBarActionState,
        listState = holdingsListState
    )

    val tabPagerState = rememberPagerState(pageCount = { Screen.defaultBottomTabs.size })

    NavHost(
        navController = navController,
        startDestination = Screen.Overview.route,
        modifier = modifier,
        enterTransition = { fadeIn(tween(200)) + slideInHorizontally(tween(200)) { it / 4 } },
        exitTransition = { fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { -it / 4 } },
        popEnterTransition = { fadeIn(tween(200)) + slideInHorizontally(tween(200)) { -it / 4 } },
        popExitTransition = { fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { it / 4 } }
    ) {
        val onNavigateToHoldings = {
            navController.navigate(Screen.Holdings.route) {
                launchSingleTop = true
            }
        }

        val renderTabs: @Composable (targetTab: Screen) -> Unit = { targetTab ->
            val targetIndex = Screen.defaultBottomTabs.indexOf(targetTab).coerceAtLeast(0)
            LaunchedEffect(targetIndex) {
                if (tabPagerState.currentPage != targetIndex) {
                    tabPagerState.scrollToPage(targetIndex)
                }
            }
            LyndTabPager(
                pagerState = tabPagerState,
                overviewViewModel = overviewViewModel,
                plannerViewModel = plannerViewModel,
                payoutsViewModel = payoutsViewModel,
                overviewListState = overviewListState,
                plannerListState = plannerListState,
                payoutsListState = payoutsListState,
                bottomBarActionState = bottomBarActionState,
                onNavigateToHoldings = onNavigateToHoldings
            )
        }

        composable(Screen.Overview.route) {
            renderTabs(Screen.Overview)
        }
        composable(Screen.Planner.route) {
            renderTabs(Screen.Planner)
        }
        composable(Screen.Payouts.route) {
            renderTabs(Screen.Payouts)
        }
        composable(Screen.Holdings.route) {
            HoldingsScreen(
                viewModel = holdingsViewModel,
                listState = holdingsListState,
                onActionChange = { action ->
                    bottomBarActionState?.setAction(Screen.Holdings.route, action)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
private fun RegisterScrollToTopHandler(
    route: String,
    bottomBarActionState: BottomBarActionState?,
    listState: LazyListState
) {
    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(route, bottomBarActionState, listState) {
        bottomBarActionState?.setReselectHandler(route) {
            coroutineScope.launch {
                val layoutInfo = listState.layoutInfo
                val visibleItems = layoutInfo.visibleItemsInfo
                val firstVisible = visibleItems.firstOrNull()
                val totalDistance = if (firstVisible != null && firstVisible.index > 0) {
                    val avgItemSize = visibleItems.map { it.size }.average().toFloat().coerceAtLeast(100f)
                    (firstVisible.index * avgItemSize) + listState.firstVisibleItemScrollOffset
                } else {
                    listState.firstVisibleItemScrollOffset.toFloat()
                }

                if (totalDistance > 0f) {
                    // Decelerate curve: fast start (0.165), descending speed towards top (1.0)
                    listState.animateScrollBy(
                        value = -totalDistance,
                        animationSpec = tween(
                            durationMillis = LyndLayoutDefaults.ScrollToTopDurationMs,
                            easing = CubicBezierEasing(0.165f, 0.84f, 0.44f, 1f)
                        )
                    )
                }
                listState.scrollToItem(0)
            }
        }
        onDispose {
            bottomBarActionState?.clearReselectHandler(route)
        }
    }
}
