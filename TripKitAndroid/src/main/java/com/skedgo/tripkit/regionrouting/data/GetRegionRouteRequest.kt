package com.skedgo.tripkit.regionrouting.data

import com.google.gson.annotations.SerializedName

data class GetRegionRouteRequest(
        val region: String,
        val query: String? = null,
        @SerializedName("operatorID")
        val operatorId: String? = null,
        val modes: List<String>? = null,
        @SerializedName("routesIDs")
        val routesIds: List<String> = emptyList(),
        val routesNames: List<String> = emptyList(),
        val onlyRealTime: Boolean = false,
        val full: Boolean = false
)
