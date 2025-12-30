package com.yong.taximeter.common.util

import kotlinx.coroutines.flow.StateFlow

/**
 * Location Util
 * - Expected implementation (Android, iOS)
 * - Listens about Location status, and get Speed as StateFlow
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object LocationUtil {
    // Speed State
    val speed: StateFlow<Float>

    // Start/Stop Listening
    fun startListening()
    fun stopListening()
}