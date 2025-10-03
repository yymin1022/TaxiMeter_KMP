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
import org.jetbrains.compose.resources.stringResource
import taximeter.composeapp.generated.resources.Res
import taximeter.composeapp.generated.resources.tab_title_home
import taximeter.composeapp.generated.resources.tab_title_setting
import taximeter.composeapp.generated.resources.tab_title_store

/**
 * Setting Screen Tab
 */
internal object SettingTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Settings)
            val title = stringResource(Res.string.tab_title_setting)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(
            screen = SettingScreen
        )
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
            val title = stringResource(Res.string.tab_title_home)

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(
            screen = HomeScreen
        )
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
            val title = stringResource(Res.string.tab_title_store)

            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(
            screen = StoreScreen
        )
    }
}