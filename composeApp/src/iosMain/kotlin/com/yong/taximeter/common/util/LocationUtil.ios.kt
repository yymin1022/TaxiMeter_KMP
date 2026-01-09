package com.yong.taximeter.common.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLLocationAccuracyBestForNavigation
import platform.Foundation.timeIntervalSinceDate
import platform.darwin.NSObject

/**
 * Location Util
 * - Actual implementation (iOS)
 * - Listens about Location status, and get Speed as StateFlow
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object LocationUtil {
    // Speed State
    private val _speed = MutableStateFlow(0f)
    actual val speed: StateFlow<Float> = _speed.asStateFlow()

    // CL Location Manager
    private val locationManager = CLLocationManager()

    // Location Update Callback Delegate
    private val delegate = LocationDelegate { newSpeed ->
        _speed.value = newSpeed
    }

    init {
        // Set LocationManager Delegate
        locationManager.delegate = delegate
        // Set LocationManager Accuracy
        // - BestForNavigation is the most accuracy in iOS SDK
        locationManager.desiredAccuracy = kCLLocationAccuracyBestForNavigation
    }

    /**
     * Start Listening for Location Update
     * - Actual implementation (iOS)
     */
    actual fun startListening() {
        // Location Update Request
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
    }

    /**
     * Stop Listening for Location Update
     * - Actual implementation (iOS)
     */
    actual fun stopListening() {
        // Remove Location Update
        locationManager.stopUpdatingLocation()
        // Reset saved values
        _speed.value = 0f
        delegate.reset()
    }

    // Delegate for Location Update
    private class LocationDelegate(
        val onSpeedUpdate: (Float) -> Unit
    ): NSObject(), CLLocationManagerDelegateProtocol {
        // Previous Location info for calculation
        private var prevLocation: CLLocation? = null

        // Reset saved values
        fun reset() {
            prevLocation = null
        }

        @OptIn(ExperimentalForeignApi::class)
        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
            didUpdateLocations.lastOrNull()?.let { nextLocation ->
                if(nextLocation !is CLLocation) return

                // If previous location info is unavailable, update it
                if(prevLocation == null) {
                    onSpeedUpdate(0f)
                    prevLocation = nextLocation
                    return
                }

                // Get delta time (Second)
                val deltaTime = nextLocation.timestamp.timeIntervalSinceDate(prevLocation!!.timestamp)
                if(deltaTime <= 0) return

                // Get moved distance (Meter)
                val distance = nextLocation.distanceFromLocation(prevLocation!!)

                // Get moving speed by Distance and Delta time (m/s)
                val speed = distance / deltaTime

                // Update Speed State
                onSpeedUpdate(speed.toFloat())
                // Update Previous Location
                prevLocation = nextLocation
            }
        }
    }
}