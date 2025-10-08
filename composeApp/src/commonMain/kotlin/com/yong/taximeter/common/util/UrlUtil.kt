package com.yong.taximeter.common.util

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object UrlUtil: KoinComponent {
    const val URL_DEVELOPER_BLOG = "https://dev-lr.com"
    const val URL_DEVELOPER_GITHUB = "https://github.com/yymin1022"
    const val URL_DEVELOPER_INSTAGRAM = "https://instagram.com/useful_min"
    const val URL_PRIVACY_POLICY = "https://defcon.or.kr/privacy"
    private val urlLauncher: UrlLauncher by inject()

    fun openUrl(url: String) {
        urlLauncher.openUrl(url)
    }
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class UrlLauncher {
    fun openUrl(url: String)
}