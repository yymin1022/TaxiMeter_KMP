package com.yong.taximeter.ui.main.subscreen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel

object HomeScreen: Screen {
    @Composable
    override fun Content() {
        val viewModel: HomeViewModel = getScreenModel()
        val uiState = viewModel.uiState.value

        HomeScreenInternal(
            modifier = Modifier
                .fillMaxSize(),
            uiState = uiState
        )
    }

    @Composable
    private fun HomeScreenInternal(
        modifier: Modifier = Modifier,
        uiState: HomeUiState
    ) {
        Box(
            modifier = modifier
        ) {
            // TODO: Home UI 구현
            Text("Home Screen")
        }
    }
}