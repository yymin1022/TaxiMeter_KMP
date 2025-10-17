package com.yong.taximeter.ui.meter

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yong.taximeter.common.model.CostInfo
import com.yong.taximeter.common.model.CostMode
import com.yong.taximeter.common.util.CostUtil
import com.yong.taximeter.common.util.PreferenceUtil
import com.yong.taximeter.common.util.PreferenceUtil.KEY_SETTING_LOCATION
import com.yong.taximeter.ui.main.subscreen.setting.model.LocationSetting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MeterUiState(
    val curCost: Int = 0,
    val curCostMode: CostMode = CostMode.MODE_BASE,
    val curCounter: Int = 0,
    val curDistance: Float = 0f,
    val curSpeed: Float = 0f,

    val isDriving: Boolean = false,
    val isNightPerc: Boolean = false,
    val isOutCityPerc: Boolean = false,
)

class MeterViewModel: ScreenModel {
    private val _uiState = MutableStateFlow(MeterUiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var costInfo: CostInfo

    init {
        screenModelScope.launch {
            val curLocationPref = PreferenceUtil.getString(KEY_SETTING_LOCATION, "")
            val curLocation = LocationSetting.fromKey(curLocationPref)
            this@MeterViewModel.costInfo = CostUtil.getCostForLocation(curLocation)

            setupCostState()
        }
    }

    fun startDriving() {
        _uiState.update { it.copy(isDriving = true) }
    }

    fun stopDriving() {
        _uiState.update { it.copy(isDriving = false) }
    }

    fun updateNightPerc(isEnabled: Boolean) {
        _uiState.update { it.copy(isNightPerc = isEnabled) }
    }

    fun updateOutCityPerc(isEnabled: Boolean) {
        _uiState.update { it.copy(isOutCityPerc = isEnabled) }
    }

    private fun setupCostState() {
        _uiState.update {
            it.copy(
                curCost = this.costInfo.costBase,
                curCostMode = CostMode.MODE_BASE,
                curCounter = this.costInfo.distBase,
                curDistance = 0f,
                curSpeed = 0f,
                isDriving = false,
                isNightPerc = false,
                isOutCityPerc = false,
            )
        }
    }
}