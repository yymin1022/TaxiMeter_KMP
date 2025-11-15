package com.yong.taximeter.common.manager

import kotlinx.coroutines.flow.StateFlow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object LocationManagerFactory {
    fun create(): LocationManager
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class LocationManager {
    val speed: StateFlow<Float>

    fun startListening()
    fun stopListening()
}