package com.skedgo.tripkit.data.tsp

import com.skedgo.tripkit.routing.ModeInfo

data class Mode(
    val modeInfo: ModeInfo,
    val specificModes: List<Mode>,
    val title: String? = "",
    val url: String? = ""
)