package com.yong.taximeter

import android.app.Application
import com.yong.taximeter.di.appModule
import com.yong.taximeter.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TaxiMeterApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Common App Module init
            androidContext(this@TaxiMeterApplication)
            modules(appModule, platformModule)
        }
    }
}