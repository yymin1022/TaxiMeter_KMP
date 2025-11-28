package com.yong.taximeter

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.PurchasesConfiguration
import com.yong.taximeter.common.manager.LocationManagerFactory
import com.yong.taximeter.common.util.PermissionUtil
import com.yong.taximeter.di.ActivityProvider
import com.yong.taximeter.di.ActivityProviderImpl
import com.yong.taximeter.di.androidModule
import com.yong.taximeter.di.appModule
import com.yong.taximeter.di.platformModule
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.get
import secrets.Secrets

class TaxiMeterApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        Firebase.initialize(this)

        // Location Manager Init
        LocationManagerFactory.init(this)

        // RevenueCat Init
        val builder = PurchasesConfiguration.Builder(Secrets.REVENUECAT_API_KEY_ANDROID)
        Purchases.configure(builder.build())

        startKoin {
            // Common App Module init
            androidContext(this@TaxiMeterApplication)
            modules(androidModule, appModule, platformModule)
        }

        // Activity Provider 초기화
        val activityProvider = get<ActivityProvider>(ActivityProviderImpl::class.java)
        registerActivityLifecycleCallbacks(object: ActivityLifecycleCallbacks {
            // Activity Start, Stop 기준으로 Provider 업데이트 호출
            override fun onActivityStarted(activity: Activity) {
                activityProvider.setCurrentActivity(activity)
            }

            override fun onActivityStopped(activity: Activity) {
                if(activityProvider.getCurrentActivity() === activity) {
                    activityProvider.setCurrentActivity(null)
                }
            }

            override fun onActivityCreated(a: Activity, b: Bundle?) {}
            override fun onActivityDestroyed(a: Activity) {}
            override fun onActivityPaused(a: Activity) {}
            override fun onActivityResumed(a: Activity) {}
            override fun onActivitySaveInstanceState(a: Activity, b: Bundle) {}
        })
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