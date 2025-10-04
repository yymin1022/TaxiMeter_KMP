package com.yong.taximeter.ui.main.subscreen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
        // 전체 UI를 Column으로 배치
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Icon 영역
            HomeIcon(
                modifier = Modifier,
            )

            // Title 영역
            HomeTitle(
                modifier = Modifier,
            )

            // Description 영역
            HomeTitleDescription(
                modifier = Modifier,
            )
        }
    }

    @Composable
    private fun HomeIcon(
        modifier: Modifier = Modifier
    ) {
        // TODO: Home Icon 구현
        Text("Home Icon")
    }

    @Composable
    private fun HomeTitle(
        modifier: Modifier = Modifier
    ) {
        // TODO: Home Title 구현
        Text("Home Title")
    }

    @Composable
    private fun HomeTitleDescription(
        modifier: Modifier = Modifier
    ) {
        // TODO: Home Title Description 구현
        Text("Home Title Description")
    }
}