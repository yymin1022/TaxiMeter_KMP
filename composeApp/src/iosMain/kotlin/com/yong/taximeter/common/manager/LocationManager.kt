package com.yong.taximeter.common.manager

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

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object LocationManagerFactory {
    actual fun create(): LocationManager? = LocationManager()
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
        // 저장된 정보 초기화
        delegate.reset()
        _speed.value = 0f
    }

    // Location Update를 받아 처리하기 위한 Delegate
    private class LocationDelegate(
        val onSpeedUpdate: (Float) -> Unit
    ): NSObject(), CLLocationManagerDelegateProtocol {
        // Location 정보 변화값 계산을 위한 직전 위치
        private var prevLocation: CLLocation? = null

        // 저장된 정보 초기화
        fun reset() {
            prevLocation = null
        }

        @OptIn(ExperimentalForeignApi::class)
        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
            didUpdateLocations.lastOrNull()?.let { nextLocation ->
                if(nextLocation !is CLLocation) return

                // 이전 위치가 유효하지 않은 경우, Speed 정보를 0으로 지정하고 종료
                if(prevLocation == null) {
                    onSpeedUpdate(0f)
                    prevLocation = nextLocation
                    return
                }

                // 시간 Delta 계산 (Second)
                val deltaTime = nextLocation.timestamp.timeIntervalSinceDate(prevLocation!!.timestamp)
                if(deltaTime <= 0) return

                // 이동거리 계산 (Meter)
                val distance = nextLocation.distanceFromLocation(prevLocation!!)

                // 이동속도 계산 (m/s)
                val speed = distance / deltaTime

                // Speed State 업데이트
                onSpeedUpdate(speed.toFloat())
                // 직전 위치 정보 업데이트
                prevLocation = nextLocation
            }
        }
    }
}