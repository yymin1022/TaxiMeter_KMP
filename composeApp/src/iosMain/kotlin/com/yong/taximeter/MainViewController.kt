package com.yong.taximeter

import androidx.compose.ui.window.ComposeUIViewController
import com.yong.taximeter.di.appModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin {
            modules(appModule)
        }
    }
) { App() }