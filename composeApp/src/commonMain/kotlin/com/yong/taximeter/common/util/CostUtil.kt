package com.yong.taximeter.common.util

import com.yong.taximeter.common.model.CostInfo
import com.yong.taximeter.ui.main.subscreen.setting.model.LocationSetting
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.serialization.Serializable

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
     * Get cost info of [location]
     */
    suspend fun getCostForLocation(location: LocationSetting): CostInfo {
        val cityKey = location.key

        val percNight1 = PreferenceUtil.getInt("pref_cost_${cityKey}_perc_night_1", 20)
        val percNight2 = PreferenceUtil.getInt("pref_cost_${cityKey}_perc_night_2", 40)

        return CostInfo(
            costBase = PreferenceUtil.getInt("pref_cost_${cityKey}_cost_base", 4800),
            distBase = PreferenceUtil.getInt("pref_cost_${cityKey}_dist_base", 1600),
            costRunPer = PreferenceUtil.getInt("pref_cost_${cityKey}_cost_run_per", 131),
            costTimePer = PreferenceUtil.getInt("pref_cost_${cityKey}_cost_time_per", 30),
            percCity = PreferenceUtil.getInt("pref_cost_${cityKey}_perc_city", 20),
            percNight1 = percNight1,
            percNight1From = PreferenceUtil.getInt("pref_cost_${cityKey}_perc_night_start_1", 22),
            percNight1To = PreferenceUtil.getInt("pref_cost_${cityKey}_perc_night_end_1", 4),
            percNightIs2 = percNight1 != percNight2,
            percNight2 = percNight2,
            percNight2From = PreferenceUtil.getInt("pref_cost_${cityKey}_perc_night_start_2", 23),
            percNight2To = PreferenceUtil.getInt("pref_cost_${cityKey}_perc_night_end_2", 2)
        )
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
            val dataList = costInfoDoc.get<List<FirestoreCostInfo>>("data")

            dataList.forEach { cityData ->
                val cityKey = cityData.city
                val costDetails = cityData.data

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

@Serializable
private data class FirestoreCostInfo(
    val city: String,
    val data: Map<String, Long>
)