package com.yong.taximeter.ui.main.subscreen.setting

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yong.taximeter.common.PreferenceUtil
import com.yong.taximeter.common.PreferenceUtil.KEY_SETTING_LOCATION
import com.yong.taximeter.common.PreferenceUtil.KEY_SETTING_THEME
import com.yong.taximeter.ui.main.subscreen.setting.model.LocationSetting
import com.yong.taximeter.ui.main.subscreen.setting.model.ThemeSetting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingUiState(
    val curSettingLocation: LocationSetting = LocationSetting.SEOUL,
    val curSettingTheme: ThemeSetting = ThemeSetting.HORSE,
)

class SettingViewModel: ScreenModel {
    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getCurrentSetting()
    }

    fun updateLocationSetting(newLocation: LocationSetting) {
        screenModelScope.launch {
            PreferenceUtil.putString(KEY_SETTING_LOCATION, newLocation.key)
            _uiState.update { it.copy(curSettingLocation = newLocation) }
        }
    }

    fun updateThemeSetting(newTheme: ThemeSetting) {
        screenModelScope.launch {
            PreferenceUtil.putString(KEY_SETTING_THEME, newTheme.key)
            _uiState.update { it.copy(curSettingTheme = newTheme) }
        }
    }

    private fun getCurrentSetting() {
        screenModelScope.launch {
            // Get current preference
            val prefSettingLocation = PreferenceUtil.getString(KEY_SETTING_LOCATION, LocationSetting.SEOUL.key)
            val prefSettingTheme = PreferenceUtil.getString(KEY_SETTING_THEME, ThemeSetting.HORSE.key)

            // Convert preference string key to Enum
            val curSettingLocation = LocationSetting.fromKey(prefSettingLocation)
            val curSettingTheme = ThemeSetting.fromKey(prefSettingTheme)

            // Update State
            _uiState.update {
                it.copy(
                    curSettingLocation = curSettingLocation,
                    curSettingTheme = curSettingTheme,
                )
            }
        }
    }
}