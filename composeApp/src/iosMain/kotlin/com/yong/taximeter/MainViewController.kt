package com.yong.taximeter

import androidx.compose.ui.window.ComposeUIViewController
import com.yong.taximeter.di.appModule
import com.yong.taximeter.di.platformModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin {
            modules(appModule, platformModule)
        }
    }
) { App() }