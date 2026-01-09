package com.yong.taximeter.common.util

import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse

/**
 * Permission Util
 * - Actual implementation (iOS)
 * - Check or Request Location Permission
 */
@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual object PermissionUtil {
    // CLLocation Manager
    private val locationManager = CLLocationManager()

    /**
     * Check if location permission is granted
     * - Actual implementation (iOS)
     *
     * @return Flag value about location permission is granted
     */
    actual fun isLocationPermissionGranted(): Boolean {
        // Check for CLLocationManager Status
        val authStatus = locationManager.authorizationStatus()
        // Get permission granted status
        val isGranted = when(authStatus) {
            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusAuthorizedAlways -> true
            else -> false
        }

        return isGranted
    }

    /**
     * Request for location permission
     * - Actual implementation (Android)
     */
    actual fun requestLocationPermission() {
        // Request to CLLocationManager
        locationManager.requestWhenInUseAuthorization()
    }
}