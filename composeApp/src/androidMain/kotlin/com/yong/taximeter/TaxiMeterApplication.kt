package com.yong.taximeter

import android.app.Application
import com.yong.taximeter.di.appModule
import com.yong.taximeter.di.platformModule
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TaxiMeterApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        Firebase.initialize(this)

        startKoin {
            // Common App Module init
            androidContext(this@TaxiMeterApplication)
            modules(appModule, platformModule)
        }
    }
}