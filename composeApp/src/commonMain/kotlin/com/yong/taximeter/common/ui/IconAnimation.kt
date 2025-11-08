package com.yong.taximeter.common.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun IconAnimation(
    modifier: Modifier = Modifier,
    animationDuration: Int,
    iconResList: List<DrawableResource>,
) {
    if(iconResList.isEmpty()) return

    val animationValue by if(animationDuration > 0) {
        val transition = rememberInfiniteTransition(
            label = "icon_animation_transition"
        )

        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = animationDuration,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "icon_animation_value"
        )
    } else {
        remember { mutableStateOf(0f) }
    }

    val frameCnt = iconResList.size
    val frameIdx by remember(animationValue) {
        derivedStateOf {
            (animationValue * frameCnt).toInt() % frameCnt
        }
    }

    Image(
        modifier = modifier,
        painter = painterResource(iconResList[frameIdx]),
        contentDescription = null,
    )
}