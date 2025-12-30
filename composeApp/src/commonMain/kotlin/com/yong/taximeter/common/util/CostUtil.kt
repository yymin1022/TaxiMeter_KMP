package com.yong.taximeter.common.util

import com.yong.taximeter.common.model.CostInfo
import com.yong.taximeter.common.model.CostInfoKey
import com.yong.taximeter.ui.main.subscreen.setting.model.LocationSetting
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.serialization.Serializable

/**
 * Cost Util
 * - Logics about cost
 */
object CostUtil {
    // Firestore Key
    private const val FIRESTORE_KEY_COLLECTION_COST = "cost"
    private const val FIRESTORE_KEY_DOCUMENT_INFO = "info"
    private const val FIRESTORE_KEY_DOCUMENT_VERSION = "version"
    private const val FIRESTORE_KEY_DOCUMENT_FIELD_DATA = "data"

    // Preference Key
    private const val PREF_KEY_COST_DB_VERSION = "PREF_KEY_COST_DB_VERSION"

    // Preference Key for Cost Info
    // - Cost Info Key has variation for each city/key, so defined template
    private const val PREF_KEY_COST_INFO_PLACEHOLDER_CITY = "%city%"
    private const val PREF_KEY_COST_INFO_PLACEHOLDER_KEY = "%key%"
    private const val PREF_KEY_COST_INFO_TEMPLATE = "pref_cost_${PREF_KEY_COST_INFO_PLACEHOLDER_CITY}_${PREF_KEY_COST_INFO_PLACEHOLDER_KEY}"

    // Default Cost DB Version
    // - It means Local Cost DB is not updated
    private const val DEFAULT_COST_DB_VERSION = "20001022"

    // Firestore instance
    private val firestore by lazy { Firebase.firestore }

    /**
     * Get local Cost DB Version
     *
     * @return Local Cost DB Version
     */
    suspend fun getCostDbVersion(): String {
        val curVersion = PreferenceUtil.getString(PREF_KEY_COST_DB_VERSION, DEFAULT_COST_DB_VERSION)
        return curVersion
    }

    /**
     * Get cost info for specific location
     *
     * @param location Specific location for check cost info
     * @return Cost info of specific location
     */
    suspend fun getCostForLocation(location: LocationSetting): CostInfo {
        // City Key
        val cityKey = location.key
        // Default Cost info
        val defaultCostInfo = CostInfo()

        // Check Night percentage info
        // - Night percentage can be 2-step structure if 1 and 2 value if different
        val percNight1 = PreferenceUtil.getInt(
            preferenceKeyForCostInfo(cityKey, CostInfoKey.COST_INFO_KEY_PERC_NIGHT_1),
            defaultCostInfo.percNight1)
        val percNight2 = PreferenceUtil.getInt(
            preferenceKeyForCostInfo(cityKey, CostInfoKey.COST_INFO_KEY_PERC_NIGHT_2),
            defaultCostInfo.percNight2)
        val isPercNight2 = percNight1 != percNight2

        // Generate Cost Info and return
        return CostInfo(
            costBase = PreferenceUtil.getInt(
                preferenceKeyForCostInfo(cityKey, CostInfoKey.COST_INFO_KEY_COST_BASE),
                defaultCostInfo.costBase),
            distBase = PreferenceUtil.getInt(
                preferenceKeyForCostInfo(cityKey, CostInfoKey.COST_INFO_KEY_DIST_BASE),
                defaultCostInfo.distBase),
            costRunPer = PreferenceUtil.getInt(
                preferenceKeyForCostInfo(cityKey, CostInfoKey.COST_INFO_KEY_COST_RUN_PER),
                defaultCostInfo.costRunPer),
            costTimePer = PreferenceUtil.getInt(
                preferenceKeyForCostInfo(cityKey, CostInfoKey.COST_INFO_KEY_COST_TIME_PER),
                defaultCostInfo.costTimePer),
            percCity = PreferenceUtil.getInt(
                preferenceKeyForCostInfo(cityKey, CostInfoKey.COST_INFO_KEY_PERC_CITY),
                defaultCostInfo.percCity),
            percNight1 = percNight1,
            percNight1From = PreferenceUtil.getInt(
                preferenceKeyForCostInfo(cityKey, CostInfoKey.COST_INFO_KEY_PERC_NIGHT_1_FROM),
                defaultCostInfo.percNight1From),
            percNight1To = PreferenceUtil.getInt(
                preferenceKeyForCostInfo(cityKey, CostInfoKey.COST_INFO_KEY_PERC_NIGHT_1_TO),
                defaultCostInfo.percNight1To),
            percNightIs2 = isPercNight2,
            percNight2 = percNight2,
            percNight2From = PreferenceUtil.getInt(
                preferenceKeyForCostInfo(cityKey, CostInfoKey.COST_INFO_KEY_PERC_NIGHT_2_FROM),
                defaultCostInfo.percNight2From),
            percNight2To = PreferenceUtil.getInt(
                preferenceKeyForCostInfo(cityKey, CostInfoKey.COST_INFO_KEY_PERC_NIGHT_2_TO),
                defaultCostInfo.percNight2To)
        )
    }

    /**
     * Check if DB Update is available
     *
     * @return Flag value about DB update is available
     */
    suspend fun isUpdateAvailable(): Boolean {
        return try {
            // Local Cost DB Version
            val localVersion = getCostDbVersion()

            // Firestore Remote Cost DB Version
            val remoteVersionDoc = firestore
                .collection(FIRESTORE_KEY_COLLECTION_COST)
                .document(FIRESTORE_KEY_DOCUMENT_VERSION).get()
            val remoteVersion = remoteVersionDoc.get<String>(FIRESTORE_KEY_DOCUMENT_FIELD_DATA)

            // return if Local version and Remote version is same
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
            // Cost Info Firestore Document
            val costInfoDoc = firestore
                .collection(FIRESTORE_KEY_COLLECTION_COST)
                .document(FIRESTORE_KEY_DOCUMENT_INFO).get()
            val dataList = costInfoDoc.get<List<FirestoreCostInfo>>(FIRESTORE_KEY_DOCUMENT_FIELD_DATA)

            // Update cost info for each city
            dataList.forEach { cityData ->
                val cityKey = cityData.city
                val costDetails = cityData.data

                // Each cost info
                costDetails.forEach { (key, value) ->
                    val prefKey = preferenceKeyForCostInfo(cityKey, key)
                    PreferenceUtil.putInt(prefKey, value.toInt())
                }
            }

            // Update Version preference
            val remoteVersionDoc = firestore
                .collection(FIRESTORE_KEY_COLLECTION_COST)
                .document(FIRESTORE_KEY_DOCUMENT_VERSION).get()
            val remoteVersion = remoteVersionDoc.get<String>(FIRESTORE_KEY_DOCUMENT_FIELD_DATA)

            PreferenceUtil.putString(PREF_KEY_COST_DB_VERSION, remoteVersion)
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Generate preference key for each cost info
     *
     * @param city City Name for Cost Info
     * @param key Cost Info Key
     * @return Generated Preference key
     */
    private fun preferenceKeyForCostInfo(city: String, key: String): String {
        // Generate Preference Key based on template
        val prefKey = PREF_KEY_COST_INFO_TEMPLATE
            .replace(PREF_KEY_COST_INFO_PLACEHOLDER_CITY, city)
            .replace(PREF_KEY_COST_INFO_PLACEHOLDER_KEY, key)

        return prefKey
    }
}

/**
 * Serializable class for Firestore CostInfo
 */
@Serializable
private data class FirestoreCostInfo(
    val city: String,
    val data: Map<String, Long>
)