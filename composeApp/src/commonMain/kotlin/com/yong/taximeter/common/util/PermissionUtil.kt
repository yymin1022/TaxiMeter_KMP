package com.yong.taximeter.common.util

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object PermissionUtil {
    fun isLocationPermissionGranted(): Boolean
    fun requestLocationPermission()
}