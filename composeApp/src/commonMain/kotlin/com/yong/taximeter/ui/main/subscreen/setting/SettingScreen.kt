package com.yong.taximeter.ui.main.subscreen.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.yong.taximeter.common.util.UrlUtil
import com.yong.taximeter.ui.main.subscreen.setting.dialog.CommonRadioListDialog
import com.yong.taximeter.ui.main.subscreen.setting.model.LocationSetting
import com.yong.taximeter.ui.main.subscreen.setting.model.ThemeSetting
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import taximeter.composeapp.generated.resources.Res
import taximeter.composeapp.generated.resources.setting_dialog_title_location
import taximeter.composeapp.generated.resources.setting_dialog_title_theme
import taximeter.composeapp.generated.resources.setting_group_developer_info
import taximeter.composeapp.generated.resources.setting_group_meter_info
import taximeter.composeapp.generated.resources.setting_group_meter_setting
import taximeter.composeapp.generated.resources.setting_item_desc_cost_info_type1
import taximeter.composeapp.generated.resources.setting_item_desc_cost_info_type2
import taximeter.composeapp.generated.resources.setting_item_desc_developer
import taximeter.composeapp.generated.resources.setting_item_title_cost_db_info
import taximeter.composeapp.generated.resources.setting_item_title_cost_info
import taximeter.composeapp.generated.resources.setting_item_title_developer
import taximeter.composeapp.generated.resources.setting_item_title_developer_blog
import taximeter.composeapp.generated.resources.setting_item_title_developer_github
import taximeter.composeapp.generated.resources.setting_item_title_developer_instagram
import taximeter.composeapp.generated.resources.setting_item_title_location
import taximeter.composeapp.generated.resources.setting_item_title_privacy_policy
import taximeter.composeapp.generated.resources.setting_item_title_theme

object SettingScreen: Screen {
    @Composable
    override fun Content() {
        val viewModel: SettingViewModel = getScreenModel()
        val uiState = viewModel.uiState.collectAsState()

        SettingScreenInternal(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            uiState = uiState.value,
            updateLocationSetting = viewModel::updateLocationSetting,
            updateThemeSetting = viewModel::updateThemeSetting,
        )
    }

    @Composable
    private fun SettingScreenInternal(
        modifier: Modifier = Modifier,
        uiState: SettingUiState,
        updateLocationSetting: (newLocation: LocationSetting) -> Unit,
        updateThemeSetting: (newTheme: ThemeSetting) -> Unit,
    ) {
        val scrollState = rememberScrollState()
        var showLocationDialog by remember { mutableStateOf(false) }
        var showThemeDialog by remember { mutableStateOf(false) }

        val settingItemLocationTitle = uiState.curSettingLocation.titleRes
        val settingItemThemeTitle = uiState.curSettingTheme.titleRes

        val costDbVersion = uiState.costDbVersion
        val costInfo = uiState.costInfo
        val costInfoString =
            if(costInfo.percNightIs2.not()) {
                stringResource(
                    Res.string.setting_item_desc_cost_info_type1,
                    costInfo.costBase,
                    costInfo.distBase,
                    costInfo.costRunPer,
                    costInfo.costTimePer,
                    costInfo.percCity,
                    costInfo.percNight1,
                    costInfo.percNight1From,
                    costInfo.percNight1To,
                )
            } else {
                stringResource(
                    Res.string.setting_item_desc_cost_info_type2,
                    costInfo.costBase,
                    costInfo.distBase,
                    costInfo.costRunPer,
                    costInfo.costTimePer,
                    costInfo.percCity,
                    costInfo.percNight1,
                    costInfo.percNight1From,
                    costInfo.percNight1To,
                    costInfo.percNight2,
                    costInfo.percNight2From,
                    costInfo.percNight2To,
                )
            }

        LocationSettingDialog(
            showDialog = showLocationDialog,
            curSettingLocation = uiState.curSettingLocation,
            updateLocationSetting = { idx ->
                updateLocationSetting(idx)
                showLocationDialog = false
            },
            onDismiss = { showLocationDialog = false }
        )

        ThemeSettingDialog(
            showDialog = showThemeDialog,
            curSettingTheme = uiState.curSettingTheme,
            updateThemeSetting = { idx ->
                updateThemeSetting(idx)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )

        Column(
            modifier = modifier
                .verticalScroll(scrollState),
        ) {
            // Meter Setting
            SettingGroupTitle(
                modifier = Modifier,
                titleRes = Res.string.setting_group_meter_setting,
            )
            SettingItem(
                modifier = Modifier,
                titleRes = Res.string.setting_item_title_location,
                descRes = settingItemLocationTitle,
                onClick = { showLocationDialog = true },
            )
            SettingItem(
                modifier = Modifier,
                titleRes = Res.string.setting_item_title_theme,
                descRes = settingItemThemeTitle,
                onClick = { showThemeDialog = true },
            )

            // Meter Info
            SettingGroupTitle(
                modifier = Modifier,
                titleRes = Res.string.setting_group_meter_info,
            )
            SettingItem(
                modifier = Modifier,
                titleRes = Res.string.setting_item_title_cost_info,
                desc = costInfoString,
            )
            SettingItem(
                modifier = Modifier,
                titleRes = Res.string.setting_item_title_cost_db_info,
                desc = costDbVersion,
            )

            // Developer Info
            SettingGroupTitle(
                modifier = Modifier,
                titleRes = Res.string.setting_group_developer_info,
            )
            SettingItem(
                modifier = Modifier,
                titleRes = Res.string.setting_item_title_developer,
                descRes = Res.string.setting_item_desc_developer,
            )
            SettingItem(
                modifier = Modifier,
                titleRes = Res.string.setting_item_title_developer_blog,
                desc = UrlUtil.URL_DEVELOPER_BLOG,
                onClick = { UrlUtil.openUrl(UrlUtil.URL_DEVELOPER_BLOG) },
            )
            SettingItem(
                modifier = Modifier,
                titleRes = Res.string.setting_item_title_developer_github,
                desc = UrlUtil.URL_DEVELOPER_GITHUB,
                onClick = { UrlUtil.openUrl(UrlUtil.URL_DEVELOPER_GITHUB) },
            )
            SettingItem(
                modifier = Modifier,
                titleRes = Res.string.setting_item_title_developer_instagram,
                desc = UrlUtil.URL_DEVELOPER_INSTAGRAM,
                onClick = { UrlUtil.openUrl(UrlUtil.URL_DEVELOPER_INSTAGRAM) },
            )
            SettingItem(
                modifier = Modifier,
                titleRes = Res.string.setting_item_title_privacy_policy,
                desc = UrlUtil.URL_PRIVACY_POLICY,
                onClick = { UrlUtil.openUrl(UrlUtil.URL_PRIVACY_POLICY) },
            )
        }
    }

    @Composable
    private fun SettingGroupTitle(
        modifier: Modifier = Modifier,
        titleRes: StringResource,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) {
            Text(
                text = stringResource(titleRes),
                color = Color.Gray,
                fontSize = 16.sp,
            )
        }
    }

    @Composable
    private fun SettingItem(
        modifier: Modifier = Modifier,
        titleRes: StringResource? = null,
        title: String? = null,
        descRes: StringResource? = null,
        desc: String? = null,
        onClick: () -> Unit = {},
    ) {
        val titleForUI = (if(titleRes != null) stringResource(titleRes) else title) ?: ""
        val descForUI = (if(descRes != null) stringResource(descRes) else desc) ?: ""

        Column(
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 16.dp),
        ) {
            Text(
                text = titleForUI,
                color = Color.Black,
                fontSize = 16.sp,
            )
            Text(
                text = descForUI,
                color = Color.Black,
                fontSize = 14.sp,
            )
        }
    }

    @Composable
    private fun LocationSettingDialog(
        showDialog: Boolean,
        curSettingLocation: LocationSetting,
        updateLocationSetting: (newLocation: LocationSetting) -> Unit,
        onDismiss: () -> Unit
    ) {
        if(showDialog) {
            val locationOptions = LocationSetting.entries

            CommonRadioListDialog(
                titleRes = Res.string.setting_dialog_title_location,
                selectedIdx = locationOptions.indexOf(curSettingLocation),
                itemList = locationOptions.map { stringResource(it.titleRes) },
                onSelectItem = { index ->
                    updateLocationSetting(locationOptions[index])
                    onDismiss()
                },
                onCancel = onDismiss
            )
        }
    }

    @Composable
    private fun ThemeSettingDialog(
        showDialog: Boolean,
        curSettingTheme: ThemeSetting,
        updateThemeSetting: (newTheme: ThemeSetting) -> Unit,
        onDismiss: () -> Unit
    ) {
        if(showDialog) {
            val themeOptions = ThemeSetting.entries

            CommonRadioListDialog(
                titleRes = Res.string.setting_dialog_title_theme,
                selectedIdx = themeOptions.indexOf(curSettingTheme),
                itemList = themeOptions.map { stringResource(it.titleRes) },
                onSelectItem = { index ->
                    updateThemeSetting(themeOptions[index])
                    onDismiss()
                },
                onCancel = onDismiss
            )
        }
    }
}