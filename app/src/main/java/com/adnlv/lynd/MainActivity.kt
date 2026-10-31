package com.adnlv.lynd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.adnlv.lynd.ui.navigation.LyndBottomBar
import com.adnlv.lynd.ui.navigation.LyndNavHost
import com.adnlv.lynd.ui.navigation.Screen
import com.adnlv.lynd.ui.navigation.rememberBottomBarActionState
import com.adnlv.lynd.ui.theme.LocalLyndHazeState
import com.adnlv.lynd.ui.theme.LyndTheme
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appContainer = (application as LyndApplication).container

        setContent {
            LyndTheme {
                MainApp(appContainer = appContainer)
            }
        }
    }
}

@Composable
fun MainApp(appContainer: AppContainer) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val bottomBarActionState = rememberBottomBarActionState()
    val hazeState = rememberHazeState()

    val bottomTabs = Screen.defaultBottomTabs
    val tabPagerState = rememberPagerState(pageCount = { bottomTabs.size })
    val coroutineScope = rememberCoroutineScope()

    val activeTabScreen = bottomTabs.getOrElse(tabPagerState.currentPage) { Screen.Overview }
    val isAtHoldings = currentRoute == Screen.Holdings.route
    val activeRoute = if (isAtHoldings) Screen.Holdings.route else activeTabScreen.route
    val showBottomBar = Screen.shouldShowBottomBar(activeRoute, bottomTabs)

    CompositionLocalProvider(LocalLyndHazeState provides hazeState) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.statusBars,
        ) { innerPadding ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding()),
            ) {
                LyndNavHost(
                    navController = navController,
                    appContainer = appContainer,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .hazeSource(hazeState),
                    bottomBarActionState = bottomBarActionState,
                    tabPagerState = tabPagerState,
                )
                if (showBottomBar) {
                    LyndBottomBar(
                        navController = navController,
                        currentRoute = activeRoute,
                        bottomTabs = bottomTabs,
                        action = bottomBarActionState.actionForRoute(activeRoute),
                        onTabSelected = { screen ->
                            val targetIndex = bottomTabs.indexOf(screen)
                            if (targetIndex >= 0) {
                                if (isAtHoldings) {
                                    navController.popBackStack()
                                }
                                coroutineScope.launch {
                                    tabPagerState.scrollToPage(targetIndex)
                                }
                            }
                        },
                        onTabReselected = { screen ->
                            bottomBarActionState.onReselect(screen.route)
                        },
                        modifier = Modifier.align(Alignment.BottomCenter),
                    )
                }
            }
        }
    }
}
