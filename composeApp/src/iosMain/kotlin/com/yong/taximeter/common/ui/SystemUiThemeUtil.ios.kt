package com.yong.taximeter.common.ui

import androidx.compose.runtime.Composable
import platform.UIKit.NSForegroundColorAttributeName
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UINavigationBar
import platform.UIKit.UIStatusBarStyleDefault
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object SystemUiThemeUtil {
    @Composable
    actual fun setSystemUiTheme(isDark: Boolean) {
        // StatusBar 색상 지정
        // - [isDark]에 따라 Default / Light
        val statusBarStyle =
            if(isDark) UIStatusBarStyleLightContent
            else UIStatusBarStyleDefault
        UIApplication.sharedApplication.setStatusBarStyle(statusBarStyle)

        // NavigationBar 색상 지정
        val navBarBackgroundColor = if(isDark) UIColor.blackColor else UIColor.whiteColor
        val navBarIconColor = if(isDark) UIColor.whiteColor else UIColor.blackColor
        val navBar = UINavigationBar.appearance().apply {
            barTintColor = navBarBackgroundColor
            tintColor = navBarIconColor
            titleTextAttributes = mapOf<Any?, Any?>(
                NSForegroundColorAttributeName to navBarIconColor
            )
        }
    }
}