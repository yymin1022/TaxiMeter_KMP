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

    /**
     * Get latest Cost DB from server, and save as Preference
     */
    suspend fun updateCostInfo() {
        try {
            val costInfoDoc = firestore.collection("cost").document("info").get()
            val dataList = costInfoDoc.get<List<Map<String, Any>>>("data")

            dataList.forEach { cityData ->
                val cityKey = cityData["city"] as String
                val costDetails = cityData["data"] as Map<String, Long>

                costDetails.forEach { (key, value) ->
                    val prefKey = "pref_cost_${cityKey}_$key"
                    PreferenceUtil.putInt(prefKey, value.toInt())
                }
            }

            val remoteVersionDoc = firestore.collection("cost").document("version").get()
            val remoteVersion = remoteVersionDoc.get<String>("data")
            PreferenceUtil.putString(PREF_KEY_COST_DB_VERSION, remoteVersion)
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }
}