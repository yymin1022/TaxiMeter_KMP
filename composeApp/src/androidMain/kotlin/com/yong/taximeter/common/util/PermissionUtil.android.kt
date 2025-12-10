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

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual object PermissionUtil: KoinComponent {
    // Activity Result Code
    private const val REQUEST_CODE_LOCATION = 1001

    // Activity Provider
    private val activityProvider: ActivityProvider by inject()
    // Android Context
    private val context: Context by inject()

    actual fun isLocationPermissionGranted(): Boolean {
        // ACCESS_FINE_LOCATION: 정확한 위치
        val isFineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return isFineGranted
    }

    actual fun requestLocationPermission() {
        val activity = activityProvider.getCurrentActivity() ?: return
        // 이미 권한이 있으면 굳이 다시 요청할 필요 없음
        if (isLocationPermissionGranted()) return

        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE_LOCATION
        )
    }
}