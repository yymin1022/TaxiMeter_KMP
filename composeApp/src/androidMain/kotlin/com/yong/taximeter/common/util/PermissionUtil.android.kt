package com.yong.taximeter.common.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual object PermissionUtil {
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    actual fun isLocationPermissionGranted(): Boolean {
        // Context가 초기화되지 않은 경우, Permission을 확인할 수 없다
        val isGranted = context?.let { ctx ->
            // ACCESS_FINE_LOCATION: 정확한 위치
            val fineGranted = ContextCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            fineGranted
        } ?: false

        return isGranted
    }

    actual fun requestLocationPermission() {
        // TODO: Android 위치정보 권한 요청
    }
}