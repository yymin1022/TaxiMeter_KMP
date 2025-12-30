package com.yong.taximeter.common.model

/**
 * Cost Info
 * - Defines cost info datas for each city
 */
data class CostInfo(
    // 기본요금
    val costBase: Int = 4800,
    // 기본요금 주행거리
    val distBase: Int = 1600,
    // 주행요금 기준 거리
    val costRunPer: Int = 131,
    // 시간요금 기준 시간
    val costTimePer: Int = 30,
    // 시외할증 비율
    val percCity: Int = 20,
    // 야간할증 1단계 비율
    val percNight1: Int = 20,
    // 야간할증 1단계 시작 시간
    val percNight1From: Int = 22,
    // 야간할증 1단계 종료 시간
    val percNight1To: Int = 4,
    // 야간할증 2단계 여부
    val percNightIs2: Boolean = true,
    // 야간할증 2단계 비율
    val percNight2: Int = 40,
    // 야간할증 2단계 시작 시간
    val percNight2From: Int = 23,
    // 야간할증 2단계 종료 시간
    val percNight2To: Int = 2,
)

/**
 * Cost Info Key
 * - Used when map cost info and preference key
 */
object CostInfoKey {
    const val COST_INFO_KEY_COST_BASE = "cost_base"
    const val COST_INFO_KEY_DIST_BASE = "dist_base"
    const val COST_INFO_KEY_COST_RUN_PER = "cost_run_per"
    const val COST_INFO_KEY_COST_TIME_PER = "cost_time_per"
    const val COST_INFO_KEY_PERC_CITY = "perc_city"
    const val COST_INFO_KEY_PERC_NIGHT_1 = "perc_night_1"
    const val COST_INFO_KEY_PERC_NIGHT_1_FROM = "perc_night_start_1"
    const val COST_INFO_KEY_PERC_NIGHT_1_TO = "perc_night_end_1"
    const val COST_INFO_KEY_PERC_NIGHT_2 = "perc_night_2"
    const val COST_INFO_KEY_PERC_NIGHT_2_FROM = "perc_night_start_2"
    const val COST_INFO_KEY_PERC_NIGHT_2_TO = "perc_night_end_2"
}