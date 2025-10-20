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
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.yong.taximeter.common.model.CostMode
import com.yong.taximeter.common.ui.MeterColor
import com.yong.taximeter.common.ui.ShowSnackBar
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
                    .background(color = Color.Black)
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

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Bottom,
        ) {
            MeterRunnerIcon(
                modifier = Modifier,
                iconResList = emptyList(),
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
        iconResList: List<DrawableResource>,
    ) {
        // TODO: Meter Runner Icon UI 구현
        Box(
            modifier = modifier,
        ) {
            Text("Meter Runner Iconß")
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
                .fillMaxWidth(),
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
                color = Color.LightGray,
                fontSize = 20.sp,
            )

            // Description Text
            Text(
                text = desc,
                color = Color.LightGray,
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
        // TODO: Meter Control UI 구현
        Column(
            modifier = modifier,
        ) {
            Row(
                modifier = Modifier,
            ) {
                Button(
                    modifier = Modifier,
                    onClick = startDriving,
                ) {
                    Text("Start Driving")
                }
                Button(
                    modifier = Modifier,
                    onClick = stopDriving,
                ) {
                    Text("Stop Driving")
                }
            }

            Row(
                modifier = Modifier,
            ) {
                Button(
                    modifier = Modifier,
                    onClick = { onClickNightPerc() },
                ) {
                    Text("Night Perc [Current is $isNightPerc]")
                }
                Button(
                    modifier = Modifier,
                    onClick = { onClickOutCityPerc(isOutCityPerc.not()) },
                ) {
                    Text("OutCity Perc [Current is $isOutCityPerc]")
                }
            }
        }
    }
}