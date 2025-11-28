package com.yong.taximeter.common.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual object PermissionUtil: KoinComponent {
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
        // TODO: Android 위치정보 권한 요청
    }
}