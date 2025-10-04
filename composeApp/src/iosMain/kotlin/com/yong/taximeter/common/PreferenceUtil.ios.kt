package com.yong.taximeter.common

actual class PreferenceManager {
    actual suspend fun getString(key: String, defaultValue: String): String {
        // TODO: String Get 로직 구현
        return defaultValue
    }

    actual suspend fun putString(key: String, value: String) {
        // TODO: String Put 로직 구현
    }

    actual suspend fun getInt(key: String, defaultValue: Int): Int {
        // TODO: Int Get 로직 구현
        return defaultValue
    }

    actual suspend fun putInt(key: String, value: Int) {
        // TODO: Int Put 로직 구현
    }
}