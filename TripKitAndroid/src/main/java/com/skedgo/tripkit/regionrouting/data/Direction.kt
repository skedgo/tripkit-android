package com.skedgo.tripkit.regionrouting.data

data class Direction(
    val encodedShape: String,
    val id: String,
    val name: String,
    val shapeIsDetailed: Boolean,
    val stops: List<Stop>
)