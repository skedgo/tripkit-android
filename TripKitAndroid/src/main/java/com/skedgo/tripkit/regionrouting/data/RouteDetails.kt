package com.skedgo.tripkit.regionrouting.data

import com.skedgo.tripkit.routing.ModeInfo
import com.skedgo.tripkit.routing.ServiceColor

data class RouteDetails(
    val directions: List<Direction>,
    val id: String,
    val mode: String,
    val modeInfo: ModeInfo,
    val operatorId: String,
    val operatorName: String,
    val region: String,
    val routeColor: ServiceColor,
    val routeName: String,
    val shortName: String
)