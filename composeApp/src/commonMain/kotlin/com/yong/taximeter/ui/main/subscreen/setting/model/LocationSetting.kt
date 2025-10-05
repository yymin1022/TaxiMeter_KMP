package com.yong.taximeter.ui.main.subscreen.setting.model

import org.jetbrains.compose.resources.StringResource
import taximeter.composeapp.generated.resources.Res
import taximeter.composeapp.generated.resources.setting_item_location_busan
import taximeter.composeapp.generated.resources.setting_item_location_chungbuk
import taximeter.composeapp.generated.resources.setting_item_location_chungnam
import taximeter.composeapp.generated.resources.setting_item_location_custom
import taximeter.composeapp.generated.resources.setting_item_location_daegu
import taximeter.composeapp.generated.resources.setting_item_location_daejeon
import taximeter.composeapp.generated.resources.setting_item_location_gangwon
import taximeter.composeapp.generated.resources.setting_item_location_gwangju
import taximeter.composeapp.generated.resources.setting_item_location_gyeongbuk
import taximeter.composeapp.generated.resources.setting_item_location_gyeonggi
import taximeter.composeapp.generated.resources.setting_item_location_gyeongnam
import taximeter.composeapp.generated.resources.setting_item_location_incheon
import taximeter.composeapp.generated.resources.setting_item_location_jeju
import taximeter.composeapp.generated.resources.setting_item_location_jeonbuk
import taximeter.composeapp.generated.resources.setting_item_location_jeonnam
import taximeter.composeapp.generated.resources.setting_item_location_seoul
import taximeter.composeapp.generated.resources.setting_item_location_ulsan

enum class LocationSetting(
    val titleRes: StringResource,
    val key: String,
) {
    SEOUL(Res.string.setting_item_location_seoul, "seoul"),
    GANGWON(Res.string.setting_item_location_gangwon, "gangwon"),
    GYEONGGI(Res.string.setting_item_location_gyeonggi, "gyeonggi"),
    GYEONGBUK(Res.string.setting_item_location_gyeongbuk, "gyeongbuk"),
    GYEONGNAM(Res.string.setting_item_location_gyeongnam, "gyeongnam"),
    GWANGJU(Res.string.setting_item_location_gwangju, "gwangju"),
    DAEGU(Res.string.setting_item_location_daegu, "daegu"),
    DAEJEON(Res.string.setting_item_location_daejeon, "daejeon"),
    BUSAN(Res.string.setting_item_location_busan, "busan"),
    ULSAN(Res.string.setting_item_location_ulsan, "ulsan"),
    INCHEON(Res.string.setting_item_location_incheon, "incheon"),
    JEONBUK(Res.string.setting_item_location_jeonbuk, "jeonbuk"),
    JEONNAM(Res.string.setting_item_location_jeonnam, "jeonnam"),
    JEJU(Res.string.setting_item_location_jeju, "jeju"),
    CHUNGBUK(Res.string.setting_item_location_chungbuk, "chungbuk"),
    CHUNGNAM(Res.string.setting_item_location_chungnam, "chungnam"),
    CUSTOM(Res.string.setting_item_location_custom, "custom");
}