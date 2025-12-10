package com.yong.taximeter.common.util

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object LocationUtil: KoinComponent {
    // Android Context
    private val context: Context by inject()

    // Speed State
    private val _speed = MutableStateFlow(0f)
    actual val speed: StateFlow<Float> = _speed.asStateFlow()

    // Location 정보 변화값 계산을 위한 직전 위치
    private var prevLocation: Location? = null

    // GMS Fused Location Client
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // GMS Location Update Callback
    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { nextLocation ->
                // 이전 위치가 유효하지 않은 경우, Speed 정보를 0으로 지정하고 종료
                if(prevLocation == null) {
                    _speed.value = 0f
                    prevLocation = nextLocation
                    return
                }

                // 시간 Delta 계산 (Second)
                val deltaTime = (nextLocation.time - prevLocation!!.time) / 1000.0f
                if(deltaTime <= 0) return

                // 이동거리 계산 (Meter)
                val distance = nextLocation.distanceTo(prevLocation!!)

                // 이동속도 계산 (m/s)
                val speed = distance / deltaTime

                // Speed State 업데이트
                _speed.value = speed
                // 직전 위치 정보 업데이트
                prevLocation = nextLocation
            }
        }
    }

    // Start Listening
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    actual fun startListening() {
        // Location Request Data
        // - 500ms 주기로 Location Update Request
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500L)
            .setMinUpdateIntervalMillis(500L)
            .build()

        // Location Update Looper init
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    // Stop Listening
    actual fun stopListening() {
        // Location Update 해제
        fusedLocationClient.removeLocationUpdates(locationCallback)
        // 저장된 정보 초기화
        _speed.value = 0f
        prevLocation = null
    }
}