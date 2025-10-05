package com.yong.taximeter.ui.main.subscreen.setting.model

import taximeter.composeapp.generated.resources.Res
import taximeter.composeapp.generated.resources.setting_item_location_seoul
import taximeter.composeapp.generated.resources.setting_item_location_gangwon
import taximeter.composeapp.generated.resources.setting_item_location_gyeonggi
import taximeter.composeapp.generated.resources.setting_item_location_gyeongbuk
import taximeter.composeapp.generated.resources.setting_item_location_gyeongnam
import taximeter.composeapp.generated.resources.setting_item_location_gwangju
import taximeter.composeapp.generated.resources.setting_item_location_daegu
import taximeter.composeapp.generated.resources.setting_item_location_daejeon
import taximeter.composeapp.generated.resources.setting_item_location_busan
import taximeter.composeapp.generated.resources.setting_item_location_ulsan
import taximeter.composeapp.generated.resources.setting_item_location_incheon
import taximeter.composeapp.generated.resources.setting_item_location_jeonbuk
import taximeter.composeapp.generated.resources.setting_item_location_jeonnam
import taximeter.composeapp.generated.resources.setting_item_location_jeju
import taximeter.composeapp.generated.resources.setting_item_location_chungbuk
import taximeter.composeapp.generated.resources.setting_item_location_chungnam
import taximeter.composeapp.generated.resources.setting_item_location_custom
import org.jetbrains.compose.resources.StringResource

enum class LocationSetting(val titleRes: StringResource) {
    SEOUL(Res.string.setting_item_location_seoul),
    GANGWON(Res.string.setting_item_location_gangwon),
    GYEONGGI(Res.string.setting_item_location_gyeonggi),
    GYEONGBUK(Res.string.setting_item_location_gyeongbuk),
    GYEONGNAM(Res.string.setting_item_location_gyeongnam),
    GWANGJU(Res.string.setting_item_location_gwangju),
    DAEGU(Res.string.setting_item_location_daegu),
    DAEJEON(Res.string.setting_item_location_daejeon),
    BUSAN(Res.string.setting_item_location_busan),
    ULSAN(Res.string.setting_item_location_ulsan),
    INCHEON(Res.string.setting_item_location_incheon),
    JEONBUK(Res.string.setting_item_location_jeonbuk),
    JEONNAM(Res.string.setting_item_location_jeonnam),
    JEJU(Res.string.setting_item_location_jeju),
    CHUNGBUK(Res.string.setting_item_location_chungbuk),
    CHUNGNAM(Res.string.setting_item_location_chungnam),
    CUSTOM(Res.string.setting_item_location_custom),
}