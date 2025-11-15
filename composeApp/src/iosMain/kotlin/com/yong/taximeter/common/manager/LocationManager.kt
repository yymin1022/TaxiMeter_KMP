package com.yong.taximeter.common.manager

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLLocationAccuracyBestForNavigation
import platform.Foundation.NSArray
import platform.darwin.NSObject

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object LocationManagerFactory {
    actual fun create(): LocationManager = LocationManager()
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class LocationManager {
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
        // LocationManager Delegate 지정
        locationManager.delegate = delegate
        // LocationManager Accuracy 지정
        // - BestForNavigation이 정확도가 가장 높음
        locationManager.desiredAccuracy = kCLLocationAccuracyBestForNavigation
    }

    // Start Listening
    actual fun startListening() {
        // Location Update Request
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
    }

    // Stop Listening
    actual fun stopListening() {
        // Location Update 해제
        locationManager.stopUpdatingLocation()
        // Speed 정보 초기화
        _speed.value = 0f
    }

    // Location Update를 받아 처리하기 위한 Delegate
    private class LocationDelegate(
        val onSpeedUpdate: (Float) -> Unit
    ): NSObject(), CLLocationManagerDelegateProtocol {
        @OptIn(ExperimentalForeignApi::class)
        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
            didUpdateLocations.lastOrNull()?.let { location ->
                location as CLLocation

                println("Location: [${location.coordinate}] / Speed: ${location.speed}")
                // Location 정보가 유효한 경우, State Update
                // - 음수인 경우, Location 정보가 유효하지 않음
                if(location.speed >= 0) {
                    onSpeedUpdate(location.speed.toFloat())
                } else {
                    onSpeedUpdate(0f)
                }
            }
        }
    }
}