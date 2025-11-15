package com.yong.taximeter.common.manager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object LocationManagerFactory {
    actual fun create(): LocationManager = LocationManager()
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class LocationManager {
    private val _speed = MutableStateFlow(0f)
    actual val speed: StateFlow<Float> = _speed.asStateFlow()

    actual fun startListening() {
        // TODO: iOS Listening 구현
    }

    actual fun stopListening() {
        // TODO: iOS Listening 구현
    }
}