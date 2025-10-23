package com.yong.taximeter.ui.meter

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yong.taximeter.common.model.CostInfo
import com.yong.taximeter.common.model.CostMode
import com.yong.taximeter.common.util.CostUtil
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

data class MeterUiState(
    val curCost: Int = 0,
    val curCostMode: CostMode = CostMode.MODE_BASE,
    val curCounter: Int = 0,
    val curDistance: Float = 0f,
    val curSpeed: Float = 0f,

    val isDriving: Boolean = false,
    val isNightPerc: Boolean = false,
    val isOutCityPerc: Boolean = false,

    val meterAnimationIcons: List<DrawableResource> = emptyList(),

    val snackBarMessageRes: StringResource? = null,
)

class MeterViewModel: ScreenModel {
    companion object {
        private const val METER_SPEED_COST_THRESHOLD_KMH = 15f

        private const val METER_UPDATE_INTERVAL = 1000L
        private const val METER_UPDATE_NEED_INIT = -1L

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
    }

    private val _uiState = MutableStateFlow(MeterUiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var costInfo: CostInfo

    private var meterDriveJob: Job? = null
    private var lastUpdateTimeMillis: Long = -1L

    init {
        screenModelScope.launch {
            val curLocationPref = PreferenceUtil.getString(KEY_SETTING_LOCATION, "")
            val curLocation = LocationSetting.fromKey(curLocationPref)
            this@MeterViewModel.costInfo = CostUtil.getCostForLocation(curLocation)

            setCostInitialState(false)
        }

        screenModelScope.launch {
            val curThemePref = PreferenceUtil.getString(KEY_SETTING_THEME, ThemeSetting.HORSE.key)
            val curTheme = ThemeSetting.fromKey(curThemePref)
            val animationIcons = when(curTheme) {
                ThemeSetting.HORSE -> ANIMATION_ICONS_HORSE
                ThemeSetting.CIRCLE -> ANIMATION_ICONS_CIRCLE
            }

            _uiState.update { it.copy(meterAnimationIcons = animationIcons) }
        }
    }

    fun startDriving() {
        if(uiState.value.isDriving.not()) {
            startDriveJob()
        }
    }

    fun stopDriving() {
        if(uiState.value.isDriving) {
            screenModelScope.launch {
                meterDriveJob?.cancelAndJoin()
                setCostInitialState(false)
            }

        }
    }

    fun showNightPercInfo() {
        _uiState.update { it.copy(snackBarMessageRes = Res.string.meter_snackbar_nightperc_info) }
    }

    fun updateOutCityPerc(isEnabled: Boolean) {
        _uiState.update { it.copy(isOutCityPerc = isEnabled) }
    }

    fun dismissSnackBar() {
        _uiState.update { it.copy(snackBarMessageRes = null) }
    }

    private fun setCostInitialState(isDriving: Boolean) {
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

    private fun startDriveJob() {
        this.meterDriveJob = screenModelScope.launch(Dispatchers.Default) {
            setCostInitialState(isDriving = true)
            updateBaseCostIfNightPerc()

            while(true) {
                increaseCost()
                delay(METER_UPDATE_INTERVAL)
            }
        }
    }

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
        // TODO: GPS 관련 구현 후 실제 현재 속도를 m/s 단위로 받아오도록 변경
        // TODO: GPS 정확도 관련 API Check 후 UI 예외처리 필요
        val curGpsSpeed = 0f
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

    private fun isNightPercStep1(curHour: Int): Boolean {
        val nightPercFrom = costInfo.percNight1From
        val nightPercTo = costInfo.percNight1To
        return ((curHour >= 18 && curHour >= nightPercFrom)
            || (curHour <= 6 && curHour < nightPercTo))
    }

    private fun isNightPercStep2(curHour: Int): Boolean {
        val nightPercFrom = costInfo.percNight2From
        val nightPercTo = costInfo.percNight2To
        return ((curHour >= 18 && curHour >= nightPercFrom)
                || (curHour <= 6 && curHour < nightPercTo))
    }

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
}