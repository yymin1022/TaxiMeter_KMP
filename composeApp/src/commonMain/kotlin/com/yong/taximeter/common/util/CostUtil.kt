package com.yong.taximeter.common.util

object CostUtil {
    private const val PREF_KEY_COST_DB_VERSION = "PREF_KEY_COST_DB_VERSION"

    private const val DEFAULT_COST_DB_VERSION = "20001022"

    suspend fun getCostDbVersion(): String {
        val curVersion = PreferenceUtil.getString(PREF_KEY_COST_DB_VERSION, DEFAULT_COST_DB_VERSION)
        return curVersion
    }
}