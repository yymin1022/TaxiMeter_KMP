package com.yong.taximeter.ui.main.subscreen.home

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data object HomeUiState

class HomeViewModel: ScreenModel {
    private val _uiState = MutableStateFlow(HomeUiState)
    val uiState = _uiState.asStateFlow()
}