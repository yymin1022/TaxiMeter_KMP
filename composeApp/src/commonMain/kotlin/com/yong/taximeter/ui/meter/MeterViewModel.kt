package com.yong.taximeter.ui.meter

import cafe.adriel.voyager.core.model.ScreenModel
import com.yong.taximeter.common.model.CostMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MeterUiState(
    val curCost: Int = 0,
    val curCoseMode: CostMode = CostMode.MODE_BASE,
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
}