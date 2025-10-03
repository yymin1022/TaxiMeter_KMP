package com.yong.taximeter.di

import com.yong.taximeter.ui.main.MainViewModel
import com.yong.taximeter.ui.main.subscreen.home.HomeViewModel
import com.yong.taximeter.ui.main.subscreen.setting.SettingViewModel
import com.yong.taximeter.ui.main.subscreen.store.StoreViewModel
import com.yong.taximeter.ui.meter.MeterViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appModule = module {
    // ViewModel Factory
    // Root Screen ViewModels
    factoryOf(::MainViewModel)
    factoryOf(::MeterViewModel)

    // Main Subscreen ViewModels
    factoryOf(::HomeViewModel)
    factoryOf(::SettingViewModel)
    factoryOf(::StoreViewModel)
}