package com.yong.taximeter.common.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yong.taximeter.di.ActivityProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

/**
 * Permission Util
 * - Actual implementation (Android)
 * - Check or Request Location Permission
 */
@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual object PermissionUtil: KoinComponent {
    // Activity Result Code
    private const val REQUEST_CODE_LOCATION = 1001

    // Activity Provider
    private val activityProvider: ActivityProvider by inject()
    // Android Context
    private val context: Context by inject()

    /**
     * Check if location permission is granted
     * - Actual implementation (Android)
     *
     * @return Flag value about location permission is granted
     */
    actual fun isLocationPermissionGranted(): Boolean {
        // ACCESS_FINE_LOCATION: Highest Accuracy Location Permission for Android
        val isFineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return isFineGranted
    }

    /**
     * Request for location permission
     * - Actual implementation (Android)
     */
    actual fun requestLocationPermission() {
        // If already granted, skip logic
        if(isLocationPermissionGranted()) return

        // Launch permission request
        val activity = activityProvider.getCurrentActivity() ?: return
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE_LOCATION
        )
    }
}