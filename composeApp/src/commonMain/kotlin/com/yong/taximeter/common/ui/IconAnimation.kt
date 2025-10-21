package com.yong.taximeter.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun IconAnimation(
    modifier: Modifier = Modifier,
    iconResList: List<DrawableResource>,
    framePerSecond: Int,
) {
    Box(
        modifier = modifier,
    ) {
        Text("Meter Runner Iconß")
    }
}