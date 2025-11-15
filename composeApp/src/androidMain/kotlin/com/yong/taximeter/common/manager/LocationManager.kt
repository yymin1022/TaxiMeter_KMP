package com.yong.taximeter.common.manager

import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.getValue

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object LocationManagerFactory {
    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }
    actual fun create(): LocationManager = LocationManager(context)
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class LocationManager(
    private val context: Context
) {
    // Speed State
    private val _speed = MutableStateFlow(0f)
    actual val speed: StateFlow<Float> = _speed.asStateFlow()

    // GMS Fused Location Client
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // GMS Location Update Callback
    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { location ->
                println("Location: [${location.latitude}, ${location.longitude}] / Speed: ${location.speed}")
                _speed.value = if(location.hasSpeed()) location.speed else 0f
            }
        }
    }

    // Start Listening
    actual fun startListening() {
        // Location Request Data
        // - 1000ms (1s) 주기로 Location Update Request
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
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
        // Speed 정보 초기화
        _speed.value = 0f
    }
}