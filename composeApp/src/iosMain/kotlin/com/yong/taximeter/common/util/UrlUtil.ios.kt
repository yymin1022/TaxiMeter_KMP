package com.yong.taximeter.common.util

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class UrlLauncher {
    actual fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        if(nsUrl != null) {
            UIApplication.sharedApplication.openURL(
                nsUrl,
                options = mapOf<Any?, Any>(), // 추가 옵션이 없으면 빈 맵을 전달합니다.
                completionHandler = null      // URL을 연 직후에 처리할 작업이 없으면 null을 전달합니다.
            )
        }
    }
}