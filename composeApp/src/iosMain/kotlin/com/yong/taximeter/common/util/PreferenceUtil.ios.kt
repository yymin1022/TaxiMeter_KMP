package com.yong.taximeter.common.util

import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PreferenceManager {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    actual suspend fun getString(key: String, defaultValue: String): String {
        return userDefaults.stringForKey(key) ?: defaultValue
    }

    actual suspend fun putString(key: String, value: String) {
        userDefaults.setValue(value, forKey = key)
    }

    actual suspend fun getInt(key: String, defaultValue: Int): Int {
        return if (userDefaults.objectForKey(key) != null) {
            userDefaults.integerForKey(key).toInt()
        } else {
            defaultValue
        }
    }

    actual suspend fun putInt(key: String, value: Int) {
        userDefaults.setInteger(value.toLong(), forKey = key)
    }

    actual suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return if (userDefaults.objectForKey(key) != null) {
            userDefaults.boolForKey(key)
        } else {
            defaultValue
        }
    }

    actual suspend fun putBoolean(key: String, value: Boolean) {
        userDefaults.setBool(value, forKey = key)
    }
}