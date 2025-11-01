package com.yong.taximeter.ui.meter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.yong.taximeter.common.model.CostMode
import com.yong.taximeter.common.ui.IconAnimation
import com.yong.taximeter.common.ui.MeterColor
import com.yong.taximeter.common.ui.ShowSnackBar
import com.yong.taximeter.common.ui.SystemUiThemeUtil
import com.yong.taximeter.common.ui.SystemUiThemeUtil.rememberSystemUiThemeSetter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import taximeter.composeapp.generated.resources.Res
import taximeter.composeapp.generated.resources.meter_cost_text
import taximeter.composeapp.generated.resources.meter_status_cost_mode_title
import taximeter.composeapp.generated.resources.meter_status_distance_desc
import taximeter.composeapp.generated.resources.meter_status_distance_title
import taximeter.composeapp.generated.resources.meter_status_drive_status_desc_false
import taximeter.composeapp.generated.resources.meter_status_drive_status_desc_true
import taximeter.composeapp.generated.resources.meter_status_drive_status_title
import taximeter.composeapp.generated.resources.meter_status_speed_desc
import taximeter.composeapp.generated.resources.meter_status_speed_title
import kotlin.math.round

object MeterScreen: Screen {
    @Composable
    override fun Content() {
        val viewModel: MeterViewModel = getScreenModel()
        val uiState = viewModel.uiState.collectAsState()

        // System UI Theme 지정 / 해제
        val systemUiThemeSetting = rememberSystemUiThemeSetter()
        DisposableEffect(Unit) {
            systemUiThemeSetting(true)

            onDispose {
                systemUiThemeSetting(false)
            }
        }

        val snackBarHostState = remember { SnackbarHostState() }
        val snackBarMessageRes = uiState.value.snackBarMessageRes
        ShowSnackBar(
            snackBarHostState = snackBarHostState,
            messageRes = snackBarMessageRes,
            dismissSnackBar = viewModel::dismissSnackBar,
        )

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            snackbarHost = { SnackbarHost(snackBarHostState) }
        ) {
            MeterScreenInternal(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MeterColor.MeterBackground)
                    .padding(16.dp),
                uiState = uiState.value,
                startDriving = viewModel::startDriving,
                stopDriving = viewModel::stopDriving,
                onClickNightPerc = viewModel::showNightPercInfo,
                onClickOutCityPerc = viewModel::updateOutCityPerc,
            )
        }
    }

    @Composable
    private fun MeterScreenInternal(
        modifier: Modifier = Modifier,
        uiState: MeterUiState,
        startDriving: () -> Unit,
        stopDriving: () -> Unit,
        onClickNightPerc: () -> Unit,
        onClickOutCityPerc: (isEnabled: Boolean) -> Unit,
    ) {
        val curCost = uiState.curCost
        val curCounter = uiState.curCounter

        val curCostMode = uiState.curCostMode
        val curDistance = uiState.curDistance
        val curSpeed = uiState.curSpeed
        val isDriving = uiState.isDriving

        val isNightPerc = uiState.isNightPerc
        val isOutCityPerc = uiState.isOutCityPerc

        val meterAnimationDurationMillis = uiState.meterAnimationDurationMillis
        val meterAnimationIcons = uiState.meterAnimationIcons

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Bottom,
        ) {
            MeterRunnerIcon(
                modifier = Modifier,
                meterRunnerDurationMillis = meterAnimationDurationMillis,
                meterRunnerIcons = meterAnimationIcons,
            )

            MeterCost(
                modifier = Modifier,
                curCost = curCost,
                curCounter = curCounter,
            )

            MeterStatus(
                modifier = Modifier,
                curCostMode = curCostMode,
                curDistance = curDistance,
                curSpeed = curSpeed,
                isDriving = isDriving,
            )

            MeterControl(
                modifier = Modifier,
                isNightPerc = isNightPerc,
                isOutCityPerc = isOutCityPerc,
                startDriving = startDriving,
                stopDriving = stopDriving,
                onClickNightPerc = onClickNightPerc,
                onClickOutCityPerc = onClickOutCityPerc,
            )
        }
    }

    @Composable
    private fun MeterRunnerIcon(
        modifier: Modifier = Modifier,
        meterRunnerDurationMillis: Int,
        meterRunnerIcons: List<DrawableResource>,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            IconAnimation(
                modifier = modifier
                    .size(84.dp),
                animationDuration = meterRunnerDurationMillis,
                iconResList = meterRunnerIcons,
            )
        }
    }

    @Composable
    private fun MeterCost(
        modifier: Modifier = Modifier,
        curCost: Int,
        curCounter: Int,
    ) {
        val costStr = stringResource(Res.string.meter_cost_text, curCost)

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
            horizontalAlignment = Alignment.End,
        ) {
            // Cost Text
            MeterCostText(
                modifier = Modifier,
                costStr = costStr,
            )

            // Counter Text
            MeterCounterText(
                modifier = Modifier,
                counterStr = curCounter.toString(),
            )
        }
    }

    @Composable
    private fun MeterCostText(
        modifier: Modifier = Modifier,
        costStr: String,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Text(
                text = costStr,
                color = MeterColor.MeterTextColorWhite,
                fontSize = 64.sp,
            )
        }
    }

    @Composable
    private fun MeterCounterText(
        modifier: Modifier = Modifier,
        counterStr: String,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Text(
                text = counterStr,
                color = MeterColor.MeterBlue,
                fontSize = 36.sp,
            )
        }
    }

    @Composable
    private fun MeterStatus(
        modifier: Modifier = Modifier,
        curCostMode: CostMode,
        curDistance: Float,
        curSpeed: Float,
        isDriving: Boolean,
    ) {
        val curDistanceRound = round(curDistance * 10) / 10
        val curSpeedRound = round(curSpeed * 10) / 10

        val costModeTitle = stringResource(Res.string.meter_status_cost_mode_title)
        val costModeDesc = stringResource(curCostMode.msgRes)
        val distanceTitle = stringResource(Res.string.meter_status_distance_title)
        val distanceDesc = stringResource(Res.string.meter_status_distance_desc, curDistanceRound)
        val driveStatusTitle = stringResource(Res.string.meter_status_drive_status_title)
        val driveStatusDesc = stringResource(
            if(isDriving) Res.string.meter_status_drive_status_desc_true
                    else Res.string.meter_status_drive_status_desc_false)
        val speedTitle = stringResource(Res.string.meter_status_speed_title)
        val speedDesc = stringResource(Res.string.meter_status_speed_desc, curSpeedRound)

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
            ) {
                // Drive Status
                MeterStatusText(
                    modifier = Modifier,
                    title = driveStatusTitle,
                    desc = driveStatusDesc,
                    isLeftAlign = false,
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Cost Mode Status
                MeterStatusText(
                    modifier = Modifier,
                    title = costModeTitle,
                    desc = costModeDesc,
                    isLeftAlign = false,
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
            ) {
                // Speed Status
                MeterStatusText(
                    modifier = Modifier,
                    title = speedTitle,
                    desc = speedDesc,
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Distance Status
                MeterStatusText(
                    modifier = Modifier,
                    title = distanceTitle,
                    desc = distanceDesc,
                )
            }
        }
    }

    @Composable
    private fun MeterStatusText(
        modifier: Modifier = Modifier,
        title: String,
        desc: String,
        isLeftAlign: Boolean = true,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(),
            horizontalAlignment = if(isLeftAlign) Alignment.Start else Alignment.End,
        ) {
            // Title Text
            Text(
                text = title,
                color = MeterColor.MeterTextColorWhite,
                fontSize = 20.sp,
            )

            // Description Text
            Text(
                text = desc,
                color = MeterColor.MeterTextColorWhite,
                fontSize = 20.sp,
            )
        }
    }

    @Composable
    private fun MeterControl(
        modifier: Modifier = Modifier,
        isNightPerc: Boolean,
        isOutCityPerc: Boolean,
        startDriving: () -> Unit,
        stopDriving: () -> Unit,
        onClickNightPerc: () -> Unit,
        onClickOutCityPerc: (isEnabled: Boolean) -> Unit,
    ) {
        Column(
            modifier = modifier
                .padding(vertical = 4.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                // Start Driving
                MeterControlButton(
                    modifier = Modifier
                        .weight(1f),
                    backgroundColor = MeterColor.MeterBlue,
                    text = "Start Driving",
                    onClick = startDriving,
                )
                // Stop Driving
                MeterControlButton(
                    modifier = Modifier
                        .weight(1f),
                    backgroundColor = MeterColor.MeterYellow,
                    text = "Stop Driving",
                    onClick = stopDriving,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                // Night Percentage
                MeterControlButton(
                    modifier = Modifier
                        .weight(1f),
                    backgroundColor = MeterColor.MeterGreen,
                    text = "Night [$isNightPerc]",
                    onClick = onClickNightPerc,
                )
                // OutCity Percentage
                MeterControlButton(
                    modifier = Modifier
                        .weight(1f),
                    backgroundColor = MeterColor.MeterRed,
                    text = "OutCity [$isOutCityPerc]",
                    onClick = { onClickOutCityPerc(isOutCityPerc.not()) },
                )
            }
        }
    }

    @Composable
    private fun MeterControlButton(
        modifier: Modifier = Modifier,
        backgroundColor: Color = MeterColor.MeterRed,
        textColor: Color = MeterColor.MeterBtnText,
        text: String,
        onClick: () -> Unit = {},
    ) {
        Button(
            modifier = modifier
                .padding(horizontal = 4.dp, vertical = 4.dp),
            colors = ButtonColors(
                containerColor = backgroundColor,
                contentColor = textColor,
                disabledContainerColor = backgroundColor,
                disabledContentColor = MeterColor.BtnTextDisabled,
            ),
            onClick = onClick,
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 12.dp),
                text = text,
                textAlign = TextAlign.Center,
            )
        }
    }
}