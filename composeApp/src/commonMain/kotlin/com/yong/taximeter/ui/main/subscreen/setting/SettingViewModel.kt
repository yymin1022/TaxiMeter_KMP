package com.yong.taximeter.ui.main.subscreen.setting

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yong.taximeter.common.model.CostInfo
import com.yong.taximeter.common.util.CostUtil
import com.yong.taximeter.common.util.PreferenceUtil
import com.yong.taximeter.common.util.PreferenceUtil.KEY_SETTING_LOCATION
import com.yong.taximeter.common.util.PreferenceUtil.KEY_SETTING_THEME
import com.yong.taximeter.ui.main.subscreen.setting.model.LocationSetting
import com.yong.taximeter.ui.main.subscreen.setting.model.ThemeSetting
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingUiState(
    val curSettingLocation: LocationSetting = LocationSetting.SEOUL,
    val curSettingTheme: ThemeSetting = ThemeSetting.HORSE,

    val costDbVersion: String = "",
    val costInfo: CostInfo = CostInfo(),
)

class SettingViewModel: ScreenModel {
    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        screenModelScope.launch {
            val prefLocation = async {
                PreferenceUtil.getString(KEY_SETTING_LOCATION, LocationSetting.SEOUL.key)
            }.await()
            val prefTheme = async {
                PreferenceUtil.getString(KEY_SETTING_THEME, ThemeSetting.HORSE.key)
            }.await()

            val curLocation = LocationSetting.fromKey(prefLocation)
            val curTheme = ThemeSetting.fromKey(prefTheme)

            val costDbVersion = async {
                CostUtil.getCostDbVersion()
            }.await()
            val costInfo = async {
                CostUtil.getCostForLocation(curLocation)
            }.await()

            _uiState.update {
                it.copy(
                    curSettingLocation = curLocation,
                    curSettingTheme = curTheme,
                    costDbVersion = costDbVersion,
                    costInfo = costInfo
                )
            }
        }
    }

    fun updateLocationSetting(newLocation: LocationSetting) {
        screenModelScope.launch {
            // Get new cost info
            val newCostInfo = CostUtil.getCostForLocation(newLocation)
            // Set location preference
            PreferenceUtil.putString(KEY_SETTING_LOCATION, newLocation.key)

            _uiState.update {
                it.copy(
                    costInfo = newCostInfo,
                    curSettingLocation = newLocation,
                )
            }
        }
    }

    fun updateThemeSetting(newTheme: ThemeSetting) {
        screenModelScope.launch {
            PreferenceUtil.putString(KEY_SETTING_THEME, newTheme.key)
            _uiState.update { it.copy(curSettingTheme = newTheme) }
        }
    }
}