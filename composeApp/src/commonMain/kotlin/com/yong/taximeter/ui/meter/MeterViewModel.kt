package com.yong.taximeter.ui.meter

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yong.taximeter.common.model.CostInfo
import com.yong.taximeter.common.model.CostMode
import com.yong.taximeter.common.util.CostUtil
import com.yong.taximeter.common.util.LocationUtil
import com.yong.taximeter.common.util.PreferenceUtil
import com.yong.taximeter.common.util.PreferenceUtil.KEY_SETTING_LOCATION
import com.yong.taximeter.common.util.PreferenceUtil.KEY_SETTING_THEME
import com.yong.taximeter.ui.main.subscreen.setting.model.LocationSetting
import com.yong.taximeter.ui.main.subscreen.setting.model.ThemeSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import taximeter.composeapp.generated.resources.Res
import taximeter.composeapp.generated.resources.ic_circle_1
import taximeter.composeapp.generated.resources.ic_circle_2
import taximeter.composeapp.generated.resources.ic_circle_3
import taximeter.composeapp.generated.resources.ic_circle_4
import taximeter.composeapp.generated.resources.ic_circle_5
import taximeter.composeapp.generated.resources.ic_circle_6
import taximeter.composeapp.generated.resources.ic_circle_7
import taximeter.composeapp.generated.resources.ic_circle_8
import taximeter.composeapp.generated.resources.ic_horse_1
import taximeter.composeapp.generated.resources.ic_horse_2
import taximeter.composeapp.generated.resources.ic_horse_3
import taximeter.composeapp.generated.resources.meter_snackbar_nightperc_info
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Meter UI State
 */
data class MeterUiState(
    val curCost: Int = 0,
    val curCostMode: CostMode = CostMode.MODE_BASE,
    val curCounter: Int = 0,
    val curDistance: Float = 0f,
    val curSpeed: Float = 0f,

    val isDriving: Boolean = false,
    val isNightPerc: Boolean = false,
    val isOutCityPerc: Boolean = false,

    val meterAnimationDurationMillis: Int = 0,
    val meterAnimationIcons: List<DrawableResource> = emptyList(),

    val snackBarMessageRes: StringResource? = null,
)

class MeterViewModel: ScreenModel {
    companion object {
        // 거리요금 - 시간요금 기준 속도
        private const val METER_SPEED_COST_THRESHOLD_KMH = 15f
        // Cost 업데이트 관련 기준 값
        // - 업데이트 주기
        private const val METER_UPDATE_INTERVAL = 1000L
        // - 기준 시간 초기값
        private const val METER_UPDATE_NEED_INIT = -1L
        // Animation Resource
        private val ANIMATION_ICONS_HORSE = listOf(
            Res.drawable.ic_horse_1,
            Res.drawable.ic_horse_2,
            Res.drawable.ic_horse_3,
        )

        private val ANIMATION_ICONS_CIRCLE = listOf(
            Res.drawable.ic_circle_1,
            Res.drawable.ic_circle_2,
            Res.drawable.ic_circle_3,
            Res.drawable.ic_circle_4,
            Res.drawable.ic_circle_5,
            Res.drawable.ic_circle_6,
            Res.drawable.ic_circle_7,
            Res.drawable.ic_circle_8,
        )
        // Animation Frame Duration
        private val ANIMATION_FRAME_DURATION_CIRCLE = listOf(
            50f to 104,
            30f to 160,
            15f to 328,
            0f to 1000,
        )

        private val ANIMATION_FRAME_DURATION_HORSE = listOf(
            50f to 142,
            30f to 200,
            20f to 250,
            10f to 333,
            0f to 500,
        )
    }

    // UI State
    private val _uiState = MutableStateFlow(MeterUiState())
    val uiState = _uiState.asStateFlow()

    // Cost Info
    private lateinit var costInfo: CostInfo

    // Meter 동작 Coroutine Job
    private var meterDriveJob: Job? = null
    // Meter 업데이트 기준 시간
    private var lastUpdateTimeMillis: Long = METER_UPDATE_NEED_INIT

    // Animation 정보
    private lateinit var meterAnimationFrameDurations: List<Pair<Float, Int>>
    private lateinit var meterAnimationIcons: List<DrawableResource>

    init {
        // Preference의 Location 정보로 요금 정보 초기화
        screenModelScope.launch {
            val curLocationPref = PreferenceUtil.getString(KEY_SETTING_LOCATION, "")
            val curLocation = LocationSetting.fromKey(curLocationPref)
            this@MeterViewModel.costInfo = CostUtil.getCostForLocation(curLocation)

            // 요금 정보 초기화
            setCostInitialState(false)
        }

        // Preference의 Theme 정보로 Animation 정보 초기화
        screenModelScope.launch {
            val curThemePref = PreferenceUtil.getString(KEY_SETTING_THEME, ThemeSetting.HORSE.key)
            val curTheme = ThemeSetting.fromKey(curThemePref)
            when(curTheme) {
                ThemeSetting.HORSE -> {
                    meterAnimationFrameDurations = ANIMATION_FRAME_DURATION_HORSE
                    meterAnimationIcons = ANIMATION_ICONS_HORSE
                }
                ThemeSetting.CIRCLE -> {
                    meterAnimationFrameDurations = ANIMATION_FRAME_DURATION_CIRCLE
                    meterAnimationIcons = ANIMATION_ICONS_CIRCLE
                }
            }

            _uiState.update { it.copy(meterAnimationIcons = meterAnimationIcons) }
        }
    }

    // 주행 시작
    fun startDriving() {
        if(uiState.value.isDriving.not()) {
            startDriveJob()
        }
    }

    // 주행 종료
    fun stopDriving() {
        if(uiState.value.isDriving) {
            screenModelScope.launch {
                // Meter 동작 종료
                meterDriveJob?.cancelAndJoin()
                // 요금 정보 초기화
                setCostInitialState(false)
            }

        }
    }

    // 야간 할증 정보 표시
    fun showNightPercInfo() {
        _uiState.update { it.copy(snackBarMessageRes = Res.string.meter_snackbar_nightperc_info) }
    }

    // 시외 할증 활성화 정보 업데이트
    fun updateOutCityPerc(isEnabled: Boolean) {
        _uiState.update { it.copy(isOutCityPerc = isEnabled) }
    }

    // Snack Bar 초기화
    fun dismissSnackBar() {
        _uiState.update { it.copy(snackBarMessageRes = null) }
    }

    // 요금 정보 초기화
    private fun setCostInitialState(isDriving: Boolean) {
        // 업데이트 기준 시간을 초기값으로 지정
        lastUpdateTimeMillis = METER_UPDATE_NEED_INIT

        _uiState.update {
            it.copy(
                curCost = this.costInfo.costBase,
                curCostMode = CostMode.MODE_BASE,
                curCounter = this.costInfo.distBase,
                curDistance = 0f,
                curSpeed = 0f,
                isDriving = isDriving,
                isNightPerc = false,
                isOutCityPerc = false,
            )
        }
    }

    // Meter 동작 시작
    private fun startDriveJob() {
        this.meterDriveJob = screenModelScope.launch(Dispatchers.Default) {
            // 요금 정보 초기화
            setCostInitialState(isDriving = true)
            // 기본 요금에 야간 할증 정보 반영
            updateBaseCostIfNightPerc()

            while(true) {
                // 요금 정보 업데이트
                increaseCost()
                // Animation 업데이트
                updateMeterAnimation()
                // 일정 시간 대기
                delay(METER_UPDATE_INTERVAL)
            }
        }
    }

    // 요금 정보 업데이트
    @OptIn(ExperimentalTime::class)
    private fun increaseCost() {
        // 현재 SystemTime Instant
        val curMoment = Clock.System.now()
        val curTimeMillis = curMoment.toEpochMilliseconds()

        // 아직 Last UpdateTime이 초기화되지 않았다면,
        // 첫 Update이므로 요금 관련해서는 아무 작업도 하지 않는다
        if(lastUpdateTimeMillis == METER_UPDATE_NEED_INIT) {
            lastUpdateTimeMillis = curTimeMillis
            return
        }

        // Delta Time 계산
        val deltaTime = (curTimeMillis - lastUpdateTimeMillis).toFloat() / 1000f

        // 현재 GPS Speed 확인
        val curGpsSpeed = LocationUtil.getCurrentSpeed()
        val newSpeed = curGpsSpeed * 3.6f

        // 이동 거리 Update
        val curDistance = uiState.value.curDistance
        val deltaDistance = curGpsSpeed * deltaTime
        val newDistance = curDistance + (deltaDistance / 1000f)

        // CostMode
        val curCostMode = uiState.value.curCostMode
        var newCostMode = curCostMode

        // Cost 연산에 필요한 기준 값 (거리요금 / 시간요금)
        val costRunPer = costInfo.costRunPer
        val costTimePer = costInfo.costTimePer

        // Cost Counter 변화 연산
        val curCounter = uiState.value.curCounter
        var newCounter = curCounter - deltaDistance.toInt()
        if(newSpeed <= METER_SPEED_COST_THRESHOLD_KMH) {
            // 시간요금 기준 속도보다 느린 경우
            newCounter -= (costRunPer.toFloat() / costTimePer.toFloat() * deltaTime).toInt()

            // 기본요금이 아닌 거리요금 주행 중인 경우 거리요금으로 주행모드 변경
            if(curCostMode == CostMode.MODE_DISTANCE) {
                newCostMode = CostMode.MODE_TIME
            }
        } else {
            // 시간요금 기준 속도보다 빠르며,
            // 기본요금이 아닌 시간요금 주행 중인 경우 거리요금으로 주행모드 변경
            if(curCostMode == CostMode.MODE_TIME) {
                newCostMode = CostMode.MODE_DISTANCE
            }
        }

        // 심야할증 정보
        var isNightPerc = uiState.value.isNightPerc

        // Cost 변화 연산
        val curCost = uiState.value.curCost
        var newCost = curCost
        if(newCounter <= 0) {
            // Cost 정보
            var deltaCost = 100

            // 기본요금 주행 중인 경우, 주행 모드 변경
            if(curCostMode == CostMode.MODE_BASE) {
                newCostMode = if(newSpeed <= METER_SPEED_COST_THRESHOLD_KMH)
                                CostMode.MODE_TIME
                            else
                                CostMode.MODE_DISTANCE
            }

            // Counter 초기화
            newCounter += costRunPer
            // 주행 속도가 매우 빠른 경우 음수 Counter가 발생할 수 있으므로 0으로 최소값 제한
            newCounter = newCounter.coerceAtLeast(0)

            // 심야할증 적용
            val curTimeZone = TimeZone.currentSystemDefault()
            val curDateTime = curMoment.toLocalDateTime(curTimeZone)

            // 심야할증 적용 연산은 1단계 적용 여부 확인 후,
            // 2단계 여부를 확인해 최종 연산한다
            val curHour = curDateTime.hour
            if(isNightPercStep1(curHour)) {
                // 심야할증 1단계 적용 가능
                // 심야할증 State 활성화
                isNightPerc = true
                if(isNightPercStep2(curHour)) {
                    // 심야할증 2단계 적용
                    val percNight2 = costInfo.percNight2
                    deltaCost += percNight2
                } else {
                    // 심야할증 1단계 적용
                    val percNight1 = costInfo.percNight1
                    deltaCost += percNight1
                }
            } else {
                // 심야할증 State 비활성화
                isNightPerc = false
            }

            // 시외할증 적용
            val isOutCityPerc = uiState.value.isOutCityPerc
            if(isOutCityPerc) {
                val percOutCity = costInfo.percCity
                deltaCost += percOutCity
            }

            newCost = curCost + deltaCost
        }

        lastUpdateTimeMillis = curTimeMillis
        _uiState.update {
            it.copy(
                curCost = newCost,
                curCostMode = newCostMode,
                curCounter = newCounter,
                curDistance = newDistance,
                curSpeed = newSpeed,
                isNightPerc = isNightPerc,
            )
        }
    }

    // 야간 할증 1단계 적용 여부
    private fun isNightPercStep1(curHour: Int): Boolean {
        val nightPercFrom = costInfo.percNight1From
        val nightPercTo = costInfo.percNight1To
        return ((curHour >= 18 && curHour >= nightPercFrom)
            || (curHour <= 6 && curHour < nightPercTo))
    }

    // 야간 할증 2단계 적용 여부
    private fun isNightPercStep2(curHour: Int): Boolean {
        val nightPercFrom = costInfo.percNight2From
        val nightPercTo = costInfo.percNight2To
        return ((curHour >= 18 && curHour >= nightPercFrom)
                || (curHour <= 6 && curHour < nightPercTo))
    }

    // 기본 요금에 야간 할증 정보 반영
    @OptIn(ExperimentalTime::class)
    private fun updateBaseCostIfNightPerc() {
        val curTimeZone = TimeZone.currentSystemDefault()
        val curMoment = Clock.System.now()
        val curHour = curMoment.toLocalDateTime(curTimeZone).hour

        var newBaseCost = costInfo.costBase.toFloat()
        when {
            isNightPercStep2(curHour) -> {
                val percNight2 = costInfo.percNight2
                newBaseCost *= ((percNight2 + 100).toFloat() / 100f)
            }
            isNightPercStep1(curHour) -> {
                val percNight1 = costInfo.percNight1
                newBaseCost *= ((percNight1 + 100).toFloat() / 100f)
            }
        }

        _uiState.update { it.copy(curCost = newBaseCost.toInt()) }
    }

    // Animation 업데이트
    private fun updateMeterAnimation() {
        // 현재 속도 정보
        val curSpeed = uiState.value.curSpeed.toInt()
        if(curSpeed <= 0) {
            _uiState.update { it.copy(meterAnimationDurationMillis = 0) }
            return
        }

        // Frame Duration 계산
        val meterAnimationFrameDurationMillis = meterAnimationFrameDurations.find { curSpeed > it.first }?.second
            ?: meterAnimationFrameDurations.last().second

        _uiState.update { it.copy(meterAnimationDurationMillis = meterAnimationFrameDurationMillis) }
    }
}