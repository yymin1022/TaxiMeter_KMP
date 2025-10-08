package com.yong.taximeter.common.util

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object UrlUtil: KoinComponent {
    private val urlLauncher: UrlLauncher by inject()

    fun openUrl(url: String) {
        urlLauncher.openUrl(url)
    }
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class UrlLauncher {
    fun openUrl(url: String)
}