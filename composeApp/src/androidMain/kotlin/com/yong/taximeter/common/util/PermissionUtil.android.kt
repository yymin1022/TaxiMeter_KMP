package com.yong.taximeter.common.util

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual object PermissionUtil {
    actual fun isLocationPermissionGranted(): Boolean {
        // TODO: Android 위치정보 권한 여부 확인
        return false
    }

    actual fun requestLocationPermission() {
        // TODO: Android 위치정보 권한 요청
    }
}