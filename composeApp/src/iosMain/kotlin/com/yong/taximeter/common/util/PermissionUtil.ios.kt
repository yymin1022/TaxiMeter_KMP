package com.yong.taximeter.common.util

import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual object PermissionUtil {
    // CLLocation Manager
    private val locationManager = CLLocationManager()

    actual fun isLocationPermissionGranted(): Boolean {
        // CLLocationManager의 상태 확인
        val authStatus = locationManager.authorizationStatus()
        // 상태 값에 따라 권한 부여 여부 판단
        val isGranted = when(authStatus) {
            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusAuthorizedAlways -> true
            else -> false
        }

        return isGranted
    }

    actual fun requestLocationPermission() {
        locationManager.requestWhenInUseAuthorization()
    }
}