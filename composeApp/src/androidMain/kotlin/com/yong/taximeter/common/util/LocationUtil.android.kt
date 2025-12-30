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

/**
 * Location Util
 * - Actual implementation (Android)
 * - Listens about Location status, and get Speed as StateFlow
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object LocationUtil: KoinComponent {
    // Android Context (Injected by Koin)
    private val context: Context by inject()

    // Speed State
    private val _speed = MutableStateFlow(0f)
    actual val speed: StateFlow<Float> = _speed.asStateFlow()

    // Previous Location info for calculation
    private var prevLocation: Location? = null

    // GMS Fused Location Client
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // GMS Location Update Callback
    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { nextLocation ->
                // If previous location info is unavailable, update it
                if(prevLocation == null) {
                    _speed.value = 0f
                    prevLocation = nextLocation
                    return
                }

                // Get delta time (Second)
                val deltaTime = (nextLocation.time - prevLocation!!.time) / 1000.0f
                if(deltaTime <= 0) return

                // Get moved distance (Meter)
                val distance = nextLocation.distanceTo(prevLocation!!)

                // Get moving speed by Distance and Delta time (m/s)
                val speed = distance / deltaTime

                // Update Speed State
                _speed.value = speed
                // Update Previous Location
                prevLocation = nextLocation
            }
        }
    }

    // Start Listening
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    actual fun startListening() {
        // Location Request Data
        // - Location Update Request per 500ms
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
        // Remove Location Update
        fusedLocationClient.removeLocationUpdates(locationCallback)
        // Reset saved values
        _speed.value = 0f
        prevLocation = null
    }
}