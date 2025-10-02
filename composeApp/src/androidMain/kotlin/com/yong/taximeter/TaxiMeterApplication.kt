package com.yong.taximeter

import android.app.Application
import com.yong.taximeter.di.appModule
import org.koin.core.context.startKoin

class TaxiMeterApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Common App Module init
            modules(appModule)
        }
    }
}