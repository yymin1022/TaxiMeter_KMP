package com.yong.taximeter.ui.main.subscreen.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yong.taximeter.common.util.CostUtil
import com.yong.taximeter.common.util.PreferenceUtil
import com.yong.taximeter.common.util.PreferenceUtil.KEY_HISTORY_DISTANCE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class HomeUiState(
    val distance: Int = 0,
    val showDistanceForDescription: Boolean = false,
)

class HomeViewModel: ScreenModel {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        screenModelScope.launch {
            val isUpdateAvailable = CostUtil.isUpdateAvailable()
            if(isUpdateAvailable) {
                CostUtil.updateCostInfo()
            }
        }

        screenModelScope.launch {
            updateDescriptionInfo()
        }
    }

    private fun updateDescriptionInfo() {
        screenModelScope.launch {
            val distance = PreferenceUtil.getInt(KEY_HISTORY_DISTANCE, 0)
            val showDistanceForDescription =
                if(distance == 0) false
                else if(Random.nextBoolean()) false
                else true

            _uiState.update {
                it.copy(
                    distance = distance,
                    showDistanceForDescription = showDistanceForDescription,
                )
            }
        }
    }
}