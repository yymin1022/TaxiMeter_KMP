package com.yong.taximeter.common.util

import kotlinx.coroutines.flow.StateFlow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object LocationUtil {
    val speed: StateFlow<Float>

    fun startListening()
    fun stopListening()
}