package com.yong.taximeter.common.util

/**
 * Permission Util
 * - Expected implementation (Android, iOS)
 * - Check or Request Location Permission
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object PermissionUtil {
    /**
     * Check if location permission is granted
     * - Expected implementation (Android, iOS)
     *
     * @return Flag value about location permission is granted
     */
    fun isLocationPermissionGranted(): Boolean

    /**
     * Request for location permission
     * - Expected implementation (Android, iOS)
     */
    fun requestLocationPermission()
}