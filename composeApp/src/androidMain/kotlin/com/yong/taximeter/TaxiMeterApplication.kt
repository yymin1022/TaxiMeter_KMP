package com.yong.taximeter

import android.app.Application
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.PurchasesConfiguration
import com.yong.taximeter.common.manager.LocationManagerFactory
import com.yong.taximeter.common.util.PermissionUtil
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

        // Location Manager Init
        LocationManagerFactory.init(this)
        // Permission Manager Init
        PermissionUtil.init(this)

        // RevenueCat Init
        val builder = PurchasesConfiguration.Builder(Secrets.REVENUECAT_API_KEY_ANDROID)
        Purchases.configure(builder.build())

        startKoin {
            // Common App Module init
            androidContext(this@TaxiMeterApplication)
            modules(appModule, platformModule)
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()

        // Location Manager Destroy
        LocationManagerFactory.release()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        if(level >= TRIM_MEMORY_BACKGROUND) {
            // Location Manager Destroy
            LocationManagerFactory.release()
        }
    }

    override fun onTerminate() {
        super.onTerminate()

        // Location Manager Destroy
        LocationManagerFactory.release()
    }
}