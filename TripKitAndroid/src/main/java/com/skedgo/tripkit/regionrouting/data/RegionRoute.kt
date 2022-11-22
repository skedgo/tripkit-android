package com.skedgo.tripkit.regionrouting.data

import com.skedgo.tripkit.routing.ModeInfo
import com.skedgo.tripkit.routing.ServiceColor

data class RegionRoute(
        val id: String,
        val operatorID: String,
        val shortName: String,
        val mode: String,
        val numberOfServices: Int,
        val operatorId: String,
        val operatorName: String,
        val modeInfo: ModeInfo,
        val stops: List<String>,
        val routeColor: ServiceColor,
        val realTime: RealTime,
        val routeName: String,
        val routeDescription: String
)

data class RealTime(
        val alerts: Boolean,
        val positions: Boolean,
        val updates: Boolean
)
