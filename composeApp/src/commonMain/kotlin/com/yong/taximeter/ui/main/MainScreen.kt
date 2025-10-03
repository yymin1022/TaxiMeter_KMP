package com.yong.taximeter.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.yong.taximeter.ui.main.navigation.HomeTab
import com.yong.taximeter.ui.main.navigation.SettingTab
import com.yong.taximeter.ui.main.navigation.StoreTab

object MainScreen: Screen {
    @Composable
    override fun Content() {
        val viewModel: MainViewModel = getScreenModel()

        // Tab 기반 Navigator
        TabNavigator(HomeTab) {
            Scaffold(
                // Safe Area 적용
                contentWindowInsets = WindowInsets.safeDrawing,
                // Content 영역에 각 Tab에 해당하는 Screen 표시
                content = { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        CurrentTab()
                    }
                },
                // BottomBar에 NavigationBar를 통해 CurrentTab 이동
                bottomBar = {
                    NavigationBar {
                        TabNavigationItem(SettingTab)
                        TabNavigationItem(HomeTab)
                        TabNavigationItem(StoreTab)
                    }
                }
            )
        }
    }

    /**
     * Tab Navigator에서 사용할 각 Item
     */
    @Composable
    private fun RowScope.TabNavigationItem(
        tab: Tab
    ) {
        val tabNavigator = LocalTabNavigator.current
        val isTabSelected = (tabNavigator.current == tab)
        val onTabClick = { tabNavigator.current = tab }

        NavigationBarItem(
            selected = isTabSelected,
            onClick = onTabClick,
            icon = {
                Icon(
                    painter = tab.options.icon!!,
                    contentDescription = tab.options.title
                )
           },
            label = {
                Text(
                    text = tab.options.title
                )
            }
        )
    }
}