package com.yong.taximeter.common.util

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

object CostUtil {
    private const val PREF_KEY_COST_DB_VERSION = "PREF_KEY_COST_DB_VERSION"

    private const val DEFAULT_COST_DB_VERSION = "20001022"

    private val firestore = Firebase.firestore

    /**
     * Get local Cost DB Version
     */
    suspend fun getCostDbVersion(): String {
        val curVersion = PreferenceUtil.getString(PREF_KEY_COST_DB_VERSION, DEFAULT_COST_DB_VERSION)
        return curVersion
    }

    /**
     * Check if DB Update is available
     */
    suspend fun isUpdateAvailable(): Boolean {
        return try {
            val localVersion = getCostDbVersion()
            val remoteVersionDoc = firestore.collection("cost").document("version").get()
            val remoteVersion = remoteVersionDoc.get<String>("data")

            localVersion != remoteVersion
        } catch(e: Exception) {
            e.printStackTrace()
            false
        }
    }
}