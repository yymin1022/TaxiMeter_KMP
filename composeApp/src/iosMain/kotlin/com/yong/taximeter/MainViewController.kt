package com.yong.taximeter

import androidx.compose.ui.window.ComposeUIViewController
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.PurchasesConfiguration
import com.yong.taximeter.di.appModule
import com.yong.taximeter.di.platformModule
import org.koin.core.context.startKoin
import secrets.Secrets

fun MainViewController() = ComposeUIViewController(
    configure = {
        // RevenueCat Init
        val builder = PurchasesConfiguration.Builder(Secrets.REVENUECAT_API_KEY_IOS)
        Purchases.configure(builder.build())

        startKoin {
            modules(appModule, platformModule)
        }
    }
) { App() }