package com.yong.taximeter

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.PurchasesConfiguration
import com.yong.taximeter.di.appModule
import com.yong.taximeter.di.platformModule
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import secrets.Secrets
import java.lang.ref.WeakReference

class TaxiMeterApplication: Application() {
    companion object {
        var currentActivity: WeakReference<Activity>? = null
    }

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {
                currentActivity = WeakReference(activity)
            }
            override fun onActivityPaused(activity: Activity) {
                if(currentActivity?.get() == activity) {
                    currentActivity = null
                }
            }
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {
                if(currentActivity?.get() == activity) {
                    currentActivity = null
                }
            }
        })

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