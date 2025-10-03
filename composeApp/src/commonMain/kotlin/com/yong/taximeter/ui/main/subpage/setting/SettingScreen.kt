package com.yong.taximeter.ui.main.subpage.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

object SettingScreen: Screen{
    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // TODO: Setting UI 구현
            Text("Setting Screen")
        }
    }
}