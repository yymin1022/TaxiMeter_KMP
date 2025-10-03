package com.yong.taximeter.di

import com.yong.taximeter.ui.main.MainViewModel
import com.yong.taximeter.ui.meter.MeterViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appModule = module {
    // ViewModel Factory
    factoryOf(::MainViewModel)
    factoryOf(::MeterViewModel)
}