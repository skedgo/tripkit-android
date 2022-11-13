package com.skedgo.tripkit.regionrouting.data

data class GetRouteDetailsRequest(
    val operatorID: String,
    val region: String,
    val routeID: Int
)