package com.yong.taximeter.ui.main.subscreen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.yong.taximeter.ui.meter.MeterScreen
import org.jetbrains.compose.resources.stringResource
import taximeter.composeapp.generated.resources.Res
import taximeter.composeapp.generated.resources.home_description_default
import taximeter.composeapp.generated.resources.home_description_distance
import taximeter.composeapp.generated.resources.home_title_1
import taximeter.composeapp.generated.resources.home_title_2

object HomeScreen: Screen {
    @Composable
    override fun Content() {
        val rootNavigator = LocalNavigator.current?.parent ?: return
        val navigateToMeter = { rootNavigator.push(MeterScreen) }

        val viewModel: HomeViewModel = getScreenModel()
        val uiState = viewModel.uiState.collectAsState()

        HomeScreenInternal(
            modifier = Modifier
                .fillMaxSize(),
            uiState = uiState.value,
            onClick = navigateToMeter,
        )
    }

    @Composable
    private fun HomeScreenInternal(
        modifier: Modifier = Modifier,
        uiState: HomeUiState,
        onClick: () -> Unit,
    ) {
        // 전체 UI를 Column으로 배치
        Column(
            modifier = modifier
                .clickable(onClick = onClick),
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
            val distance = uiState.distance
            val showDistanceForDescription = uiState.showDistanceForDescription
            HomeTitleDescription(
                modifier = Modifier,
                distance = distance,
                showDistance = showDistanceForDescription,
            )
        }
    }

    @Composable
    private fun HomeIcon(
        modifier: Modifier = Modifier,
    ) {
        val taxiIcon = rememberVectorPainter(Icons.Default.LocalTaxi)

        Image(
            modifier = modifier
                .size(160.dp),
            painter = taxiIcon,
            contentDescription = null,
        )
    }

    @Composable
    private fun HomeTitle(
        modifier: Modifier = Modifier
    ) {
        val titleLeft = stringResource(Res.string.home_title_1)
        val titleRight = stringResource(Res.string.home_title_2)

        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = titleLeft,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = titleRight,
                fontSize = 40.sp,
            )
        }
    }

    @Composable
    private fun HomeTitleDescription(
        modifier: Modifier = Modifier,
        distance: Int = 0,
        showDistance: Boolean = false,
    ) {
        val descDefault = stringResource(Res.string.home_description_default)
        val descWithDistance = stringResource(Res.string.home_description_distance, distance)

        // showDistance Flag 상태에 따라 기본 메시지 또는 Distance 기반 메시지 표시
        val descForUI = if(showDistance) descWithDistance else descDefault

        Text(
            modifier = modifier,
            text = descForUI,
            fontSize = 20.sp,
        )
    }
}