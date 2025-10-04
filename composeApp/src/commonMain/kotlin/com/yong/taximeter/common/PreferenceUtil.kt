package com.yong.taximeter.common

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object PreferenceUtil: KoinComponent {
    const val KEY_HISTORY_DISTANCE = "KEY_HISTORY_DISTANCE"

    private val manager: PreferenceManager by inject()

    suspend fun getString(key: String, defaultValue: String) = manager.getString(key, defaultValue)
    suspend fun putString(key: String, value: String) = manager.putString(key, value)
    suspend fun getInt(key: String, defaultValue: Int) = manager.getInt(key, defaultValue)
    suspend fun putInt(key: String, value: Int) = manager.putInt(key, value)
}

expect class PreferenceManager {
    suspend fun getString(key: String, defaultValue: String): String
    suspend fun putString(key: String, value: String)

    suspend fun getInt(key: String, defaultValue: Int): Int
    suspend fun putInt(key: String, value: Int)
}