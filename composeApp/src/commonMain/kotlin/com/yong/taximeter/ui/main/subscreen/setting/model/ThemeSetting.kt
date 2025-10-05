package com.yong.taximeter.ui.main.subscreen.setting.model

import org.jetbrains.compose.resources.StringResource
import taximeter.composeapp.generated.resources.Res
import taximeter.composeapp.generated.resources.setting_item_theme_circle
import taximeter.composeapp.generated.resources.setting_item_theme_horse

enum class ThemeSetting(
    val titleRes: StringResource,
    val key: String,
) {
    CIRCLE(Res.string.setting_item_theme_circle, "circle"),
    HORSE(Res.string.setting_item_theme_horse, "horse"),
}