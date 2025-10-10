package com.yong.taximeter.common.util

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object PreferenceUtil: KoinComponent {
    const val KEY_HISTORY_DISTANCE = "KEY_HISTORY_DISTANCE"
    const val KEY_SETTING_LOCATION = "KEY_SETTING_LOCATION"
    const val KEY_SETTING_THEME = "KEY_SETTING_THEME"

    private val manager: PreferenceManager by inject()

    suspend fun getString(key: String, defaultValue: String) = manager.getString(key, defaultValue)
    suspend fun putString(key: String, value: String) = manager.putString(key, value)
    suspend fun getInt(key: String, defaultValue: Int) = manager.getInt(key, defaultValue)
    suspend fun putInt(key: String, value: Int) = manager.putInt(key, value)
    suspend fun getBoolean(key: String, defaultValue: Boolean) = manager.getBoolean(key, defaultValue)
    suspend fun putBoolean(key: String, value: Boolean) = manager.putBoolean(key, value)
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class PreferenceManager {
    suspend fun getString(key: String, defaultValue: String): String
    suspend fun putString(key: String, value: String)

    suspend fun getInt(key: String, defaultValue: Int): Int
    suspend fun putInt(key: String, value: Int)

    suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean
    suspend fun putBoolean(key: String, value: Boolean)
}