package com.yong.taximeter.ui.meter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.yong.taximeter.common.model.CostMode
import com.yong.taximeter.common.ui.ShowSnackBar
import org.jetbrains.compose.resources.DrawableResource

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
        // TODO: Meter Cost UI 구현
        Column(
            modifier = modifier,
        ) {
            Text("Meter Cost [$curCost]")
            Text("Meter Counter [$curCounter]")
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
        // TODO: Meter Status UI 구현
        Column(
            modifier = modifier,
        ) {
            Row(
                modifier = Modifier,
            ) {
                Text("Cost Mode [$curCostMode]")
                Text("Driving Status [$isDriving]")
            }

            Row(
                modifier = Modifier,
            ) {
                Text("Speed [$curSpeed]")
                Text("Distance [$curDistance]")
            }
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