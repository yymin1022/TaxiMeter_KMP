package com.yong.taximeter.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel

object MainScreen: Screen {
    @Composable
    override fun Content() {
        val viewModel: MainViewModel = getScreenModel()

        Box(
            modifier = Modifier.Companion
                .fillMaxSize()
        ) {
            // TODO: Main UI 구현
            Text("Main Screen")
        }
    }
}