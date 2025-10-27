package com.yong.taximeter.common.ui

import androidx.compose.runtime.Composable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object SystemUiThemeUtil {
    @Composable
    fun setSystemUiTheme(isDark: Boolean)
}