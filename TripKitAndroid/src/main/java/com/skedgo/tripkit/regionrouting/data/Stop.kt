package com.skedgo.tripkit.regionrouting.data

data class Stop(
    val common: Boolean,
    val lat: Int,
    val lng: Int,
    val name: String,
    val stopCode: String
)