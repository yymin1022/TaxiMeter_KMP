package com.yong.taximeter.common.model

import org.jetbrains.compose.resources.StringResource
import taximeter.composeapp.generated.resources.Res
import taximeter.composeapp.generated.resources.meter_cost_mode_base
import taximeter.composeapp.generated.resources.meter_cost_mode_distance
import taximeter.composeapp.generated.resources.meter_cost_mode_time

enum class CostMode(val msgRes: StringResource) {
    MODE_BASE(msgRes = Res.string.meter_cost_mode_base),
    MODE_DISTANCE(msgRes = Res.string.meter_cost_mode_distance),
    MODE_TIME(msgRes = Res.string.meter_cost_mode_time),
}