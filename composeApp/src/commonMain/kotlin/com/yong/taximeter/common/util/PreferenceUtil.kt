package com.yong.taximeter.common.util

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object PreferenceUtil {
    suspend fun getString(key: String, defaultValue: String): String
    suspend fun putString(key: String, value: String)

    suspend fun getInt(key: String, defaultValue: Int): Int
    suspend fun putInt(key: String, value: Int)

    suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean
    suspend fun putBoolean(key: String, value: Boolean)
}