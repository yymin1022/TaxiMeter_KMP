package com.yong.taximeter.common.manager

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object LocationManagerFactory {
    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }
    actual fun create(): LocationManager = LocationManager(context)
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class LocationManager(
    private val context: Context
) {
    private val _speed = MutableStateFlow(0f)
    actual val speed: StateFlow<Float> = _speed.asStateFlow()

    actual fun startListening() {
        // TODO: Android Listening 구현
    }

    actual fun stopListening() {
        // TODO: Android Listening 구현
    }
}