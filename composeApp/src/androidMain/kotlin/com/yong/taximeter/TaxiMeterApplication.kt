package com.yong.taximeter

import android.app.Application
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.PurchasesConfiguration
import com.yong.taximeter.di.appModule
import com.yong.taximeter.di.platformModule
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import secrets.Secrets

class TaxiMeterApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        Firebase.initialize(this)

        // RevenueCat Init
        val builder = PurchasesConfiguration.Builder(Secrets.REVENUECAT_API_KEY_ANDROID)
        Purchases.configure(builder.build())

        startKoin {
            // Common App Module init
            androidContext(this@TaxiMeterApplication)
            modules(appModule, platformModule)
        }
    }
}