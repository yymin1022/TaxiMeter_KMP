package com.yong.taximeter.di

import com.yong.taximeter.common.util.PreferenceManager
import com.yong.taximeter.common.util.UrlLauncher
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { PreferenceManager() }
    single { UrlLauncher() }
}