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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.yong.taximeter.common.model.CostMode
import com.yong.taximeter.common.ui.IconAnimation
import com.yong.taximeter.common.ui.MeterColor
import com.yong.taximeter.common.ui.ShowSnackBar
import com.yong.taximeter.common.ui.SystemUiThemeUtil.rememberSystemUiThemeSetter
import com.yong.taximeter.common.ui.dialog.BasicDialog
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import taximeter.composeapp.generated.resources.Res
import taximeter.composeapp.generated.resources.meter_cost_text
import taximeter.composeapp.generated.resources.meter_exit_dialog_description
import taximeter.composeapp.generated.resources.meter_exit_dialog_title
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
        val navigator = LocalNavigator.current ?: return
        val navigatorPop: () -> Unit = { navigator.pop() }

        val viewModel: MeterViewModel = getScreenModel()
        val uiState = viewModel.uiState.collectAsState()

        // System UI Theme 지정 / 해제
        // - Meter 화면 진입 시, Dark로 지정
        // - Meter 화면 Dispose 시, Dark 해제
        val systemUiThemeSetting = rememberSystemUiThemeSetter()
        DisposableEffect(Unit) {
            systemUiThemeSetting(true)

            onDispose {
                systemUiThemeSetting(false)
            }
        }

        // Snack Bar 표시 관련 State, Composable
        // - UI State로부터 업데이트된 메시지를 받아 Snack Bar를 표시한다
        // - 표시한 뒤에는 Snack Bar 메시지 UI State 초기화를 호출한다
        val snackBarHostState = remember { SnackbarHostState() }
        val snackBarMessageRes = uiState.value.snackBarMessageRes
        ShowSnackBar(
            snackBarHostState = snackBarHostState,
            messageRes = snackBarMessageRes,
            dismissSnackBar = viewModel::dismissSnackBar,
        )

        // Meter UI
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

        // 상단 Back Button UI
        MeterBackButton(
            modifier = Modifier,
            goBack = navigatorPop,
        )
    }

    /**
     * 상단 Back Button UI
     */
    @Composable
    private fun MeterBackButton(
        modifier: Modifier = Modifier,
        goBack: () -> Unit,
    ) {
        // Dialog 관련 State
        var showConfirmDialog by remember { mutableStateOf(false) }
        val onBackButtonClicked = { showConfirmDialog = true }
        val onDialogCancel = { showConfirmDialog = false }
        val onDialogConfirm = { goBack() }

        // State에 따라 Dialog 표시
        if(showConfirmDialog) {
            BasicDialog(
                titleRes = Res.string.meter_exit_dialog_title,
                descriptionRes = Res.string.meter_exit_dialog_description,
                onConfirm = onDialogConfirm,
                onCancel = onDialogCancel
            )
        }

        // Back Button Icon
        Box(
            modifier = modifier,
            contentAlignment = Alignment.TopStart,
        ) {
            IconButton(
                modifier = Modifier
                    .padding(8.dp),
                onClick = onBackButtonClicked,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = null
                )
            }
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
        // 요금 관련 UI State
        val curCost = uiState.curCost
        val curCounter = uiState.curCounter

        // 주행 정보 관련 UI State
        val curCostMode = uiState.curCostMode
        val curDistance = uiState.curDistance
        val curSpeed = uiState.curSpeed
        val isDriving = uiState.isDriving

        // 할증 관련 UI State
        val isNightPerc = uiState.isNightPerc
        val isOutCityPerc = uiState.isOutCityPerc

        // 애니메이션 관련 UI State
        val meterAnimationDurationMillis = uiState.meterAnimationDurationMillis
        val meterAnimationIcons = uiState.meterAnimationIcons

        // 전체 UI를 Column으로 배치
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Bottom,
        ) {
            // Meter 애니메이션
            MeterRunnerIcon(
                modifier = Modifier,
                meterRunnerDurationMillis = meterAnimationDurationMillis,
                meterRunnerIcons = meterAnimationIcons,
            )

            // Meter 요금 정보
            MeterCost(
                modifier = Modifier,
                curCost = curCost,
                curCounter = curCounter,
            )

            // Meter 주행 정보
            MeterStatus(
                modifier = Modifier,
                curCostMode = curCostMode,
                curDistance = curDistance,
                curSpeed = curSpeed,
                isDriving = isDriving,
            )

            // Meter 제어 UI
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

    /**
     * Meter 애니메이션
     */
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
                modifier = Modifier
                    .size(84.dp),
                animationDuration = meterRunnerDurationMillis,
                iconResList = meterRunnerIcons,
            )
        }
    }

    /**
     * Meter 요금 정보
     */
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
            // Meter 요금 정보 Text
            MeterCostText(
                modifier = Modifier,
                costStr = costStr,
            )

            // Meter 요금 Counter Text
            MeterCounterText(
                modifier = Modifier,
                counterStr = curCounter.toString(),
            )
        }
    }

    /**
     * Meter 요금 정보 Text
     */
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

    /**
     * Meter 요금 Counter Text
     */
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

    /**
     * Meter 주행 정보
     * - 4개 주행 정보 2x2 Column-Row로 표시
     * - 주행 상태 / 주행 속도
     * - 요금 모드 / 주행 거리
     */
    @Composable
    private fun MeterStatus(
        modifier: Modifier = Modifier,
        curCostMode: CostMode,
        curDistance: Float,
        curSpeed: Float,
        isDriving: Boolean,
    ) {
        // 거리 정보와 속도 정보는 반올림하여 UI에 표시한다
        // - 각각 km, km/h 단위로 환산
        // - 소수점 아래 1자리까지 표시
        val curDistanceRound = round(curDistance * 10) / 10
        val curSpeedRound = round(curSpeed * 10) / 10

        // 요금 모드 정보
        val costModeTitle = stringResource(Res.string.meter_status_cost_mode_title)
        val costModeDesc = stringResource(curCostMode.msgRes)
        // 주행 거리 정보
        val distanceTitle = stringResource(Res.string.meter_status_distance_title)
        val distanceDesc = stringResource(Res.string.meter_status_distance_desc, curDistanceRound)
        // 주행 상태 정보
        val driveStatusTitle = stringResource(Res.string.meter_status_drive_status_title)
        val driveStatusDesc = stringResource(
            if(isDriving) Res.string.meter_status_drive_status_desc_true
                    else Res.string.meter_status_drive_status_desc_false)
        // 주행 속도 정보
        val speedTitle = stringResource(Res.string.meter_status_speed_title)
        val speedDesc = stringResource(Res.string.meter_status_speed_desc, curSpeedRound)

        // 2x2 Column-Row로 표시
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
                // 주행 상태 정보
                MeterStatusText(
                    modifier = Modifier,
                    title = driveStatusTitle,
                    desc = driveStatusDesc,
                    isLeftAlign = false,
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 요금 모드 정보
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
                // 주행 속도 정보
                MeterStatusText(
                    modifier = Modifier,
                    title = speedTitle,
                    desc = speedDesc,
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 주행 거리 정보
                MeterStatusText(
                    modifier = Modifier,
                    title = distanceTitle,
                    desc = distanceDesc,
                )
            }
        }
    }

    /**
     * Meter 주행 관련 정보 Text
     */
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
            // 주행 관련 정보 Title Text
            Text(
                text = title,
                color = MeterColor.MeterTextColorWhite,
                fontSize = 20.sp,
            )

            // 주행 관련 정보 Description Text
            Text(
                text = desc,
                color = MeterColor.MeterTextColorWhite,
                fontSize = 20.sp,
            )
        }
    }

    /**
     * Meter 제어 UI
     * - 4개 Button을 2x2 Column-Row로 표시
     * - 주행 시작 / 주행 종료
     * - 야간 할증 / 시외 할증
     */
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
        // 2x2 Column-Row로 표시
        Column(
            modifier = modifier
                .padding(vertical = 4.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                // 주행 시작 Button
                MeterControlButton(
                    modifier = Modifier
                        .weight(1f),
                    backgroundColor = MeterColor.MeterBlue,
                    text = "Start Driving",
                    onClick = startDriving,
                )
                // 주행 종료 Button
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
                // 야간 할증 Button
                MeterControlButton(
                    modifier = Modifier
                        .weight(1f),
                    backgroundColor = MeterColor.MeterGreen,
                    text = "Night [$isNightPerc]",
                    onClick = onClickNightPerc,
                )
                // 시외 할증 Button
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

    /**
     * Meter 제어 Button
     */
    @Composable
    private fun MeterControlButton(
        modifier: Modifier = Modifier,
        backgroundColor: Color = MeterColor.MeterRed,
        textColor: Color = MeterColor.MeterBtnText,
        text: String,
        onClick: () -> Unit = {},
    ) {
        // Button에 지정된 색상과 Text 표시
        Button(
            modifier = modifier
                .padding(horizontal = 4.dp, vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = textColor,
                disabledContainerColor = backgroundColor,
                disabledContentColor = MeterColor.BtnTextDisabled
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