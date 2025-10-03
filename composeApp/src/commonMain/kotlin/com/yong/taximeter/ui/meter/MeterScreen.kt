package com.yong.taximeter.ui.meter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel

object MeterScreen: Screen {
    @Composable
    override fun Content() {
        val viewModel: MeterViewModel = getScreenModel()

        Box(
            modifier = Modifier.Companion
                .fillMaxSize()
        ) {
            // TODO: Meter UI 구현
            Text("Meter Screen")
        }
    }
}