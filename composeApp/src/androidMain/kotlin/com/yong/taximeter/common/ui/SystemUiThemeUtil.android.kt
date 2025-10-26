package com.yong.taximeter.common.ui

import androidx.core.view.WindowCompat
import com.yong.taximeter.TaxiMeterApplication

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object SystemUiThemeUtil {
    actual fun setSystemUiTheme(isDark: Boolean) {
        val activity = TaxiMeterApplication.currentActivity?.get() ?: return

        val window = activity.window ?: return
        val view = window.decorView
        val insetsController = WindowCompat.getInsetsController(window, view)

        insetsController.isAppearanceLightStatusBars = !isDark
        insetsController.isAppearanceLightNavigationBars = !isDark
    }
}