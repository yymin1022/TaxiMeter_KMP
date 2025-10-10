package com.yong.taximeter.common.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("app_preferences")

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PreferenceManager(private val context: Context) {
    actual suspend fun getString(key: String, defaultValue: String): String {
        return context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: defaultValue
        }.first()
    }

    actual suspend fun putString(key: String, value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    actual suspend fun getInt(key: String, defaultValue: Int): Int {
        return context.dataStore.data.map { preferences ->
            preferences[intPreferencesKey(key)] ?: defaultValue
        }.first()
    }

    actual suspend fun putInt(key: String, value: Int) {
        context.dataStore.edit { preferences ->
            preferences[intPreferencesKey(key)] = value
        }
    }

    actual suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return context.dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(key)] ?: defaultValue
        }.first()
    }

    actual suspend fun putBoolean(key: String, value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }
}