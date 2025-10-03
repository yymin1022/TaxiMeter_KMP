package com.yong.taximeter.ui.main.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.yong.taximeter.ui.main.subscreen.home.HomeScreen
import com.yong.taximeter.ui.main.subscreen.setting.SettingScreen
import com.yong.taximeter.ui.main.subscreen.store.StoreScreen

/**
 * Setting Screen Tab
 */
internal object SettingTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Settings)
            return remember { TabOptions(index = 1u, title = "Setting", icon = icon) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = SettingScreen)
    }
}

/**
 * Home Screen Tab
 */
internal object HomeTab: Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Home)
            return remember { TabOptions(index = 0u, title = "Home", icon = icon) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = HomeScreen)
    }
}

/**
 * Store Screen Tab
 */
internal object StoreTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.ShoppingCart)
            return remember { TabOptions(index = 2u, title = "Store", icon = icon) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = StoreScreen)
    }
}