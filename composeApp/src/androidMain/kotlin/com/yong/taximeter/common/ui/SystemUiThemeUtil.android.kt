package com.yong.taximeter.common.ui

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object SystemUiThemeUtil {
    @Composable
    actual fun setSystemUiTheme(isDark: Boolean) {
        val view = LocalView.current
        val activity = view.context as Activity
        val window = activity.window
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)

        // StatusBar 및 NavigationBar 아이콘 색상 지정
        insetsController.isAppearanceLightStatusBars = !isDark
        insetsController.isAppearanceLightNavigationBars = !isDark

        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT < 35) {
            // API 35 미만에서는 배경색상을 지정
            val color = if(isDark) Color.BLACK else Color.WHITE
            window.statusBarColor = color
            window.navigationBarColor = color
        } else {
            // API 35부터는 배경색상을 직접 지정할 수 없다
            // - 대신, Edge-to-edge 시스템에서 자동으로 맞춰준다
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }
}