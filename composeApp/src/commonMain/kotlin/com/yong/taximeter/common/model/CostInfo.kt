package com.yong.taximeter.common.model

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
