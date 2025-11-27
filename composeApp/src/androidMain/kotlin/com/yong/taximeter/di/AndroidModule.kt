package com.yong.taximeter.di

import android.app.Activity
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.lang.ref.WeakReference

/**
 * Android Activity에 접근하기 위한 Provider
 * - Android Context에 접근이 필요한 Actual Object를 위해 필요하다
 * - [Application] 초기화 시, Koin을 통해 생성된다
 * - Provider 사용 시, Koin inject를 통해 선언 후 호출한다
 */
interface ActivityProvider {
    fun getCurrentActivity(): Activity?
    fun setCurrentActivity(activity: Activity?)
}

class ActivityProviderImpl: ActivityProvider {
    // Activity Reference
    // - 해제하지 않은 경우 Context Leak이 발생할 수 있으므로,
    //   Weak Reference로 선언해 GC가 정리할 수 있도록 한다
    private var activityRef: WeakReference<Activity>? = null

    override fun getCurrentActivity(): Activity? = activityRef?.get()

    override fun setCurrentActivity(activity: Activity?) {
        activityRef = when(activity) {
            null -> null
            else -> WeakReference(activity)
        }
    }
}

val androidModule = module {
    // Activity Provider 구현 및 초기화
    single<ActivityProviderImpl> { ActivityProviderImpl() }
    // Activity Provider 사용
    single<ActivityProvider> { get<ActivityProviderImpl>() }
    // Android Context
    single<Context> { androidContext() }
}