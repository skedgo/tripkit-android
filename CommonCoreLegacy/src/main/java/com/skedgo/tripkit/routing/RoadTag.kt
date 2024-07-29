package com.skedgo.tripkit.routing

import android.graphics.Color
import com.google.gson.annotations.SerializedName

enum class RoadTag {
    @SerializedName("CYCLE-LANE")
    CYCLE_LANE,

    @SerializedName("CYCLE-TRACK")
    CYCLE_TRACK,

    @SerializedName("CYCLE-NETWORK")
    CYCLE_NETWORK,

    @SerializedName("BICYCLE-DESIGNATED")
    BICYCLE_DESIGNATED,

    @SerializedName("BICYCLE-BOULEVARD")
    BICYCLE_BOULEVARD,

    @SerializedName("SIDE-WALK")
    SIDE_WALK,

    @SerializedName("MAIN-ROAD")
    MAIN_ROAD,

    @SerializedName("SIDE-ROAD")
    SIDE_ROAD,

    @SerializedName("SHARED-ROAD")
    SHARED_ROAD,

    /* Ignored
    @SerializedName("SERVICE_ROAD")
    SERVICE_ROAD,
    */
    @SerializedName("STREET-LIGHT")
    STREET_LIGHT,

    @SerializedName("CCTV-CAMERA")
    CCTV_CAMERA,

    /* Ignored
    @SerializedName("SEGREGATED")
    SEGREGATED,
    */
    @SerializedName("UNKNOWN")
    UNKNOWN,

    @SerializedName("LIT-ROUTE")
    LIT_ROUTE
}

fun String.parseRoadTag(): RoadTag =
    try {
        RoadTag.valueOf(this.replace("-", "_"))
    } catch (e: Exception) {
        RoadTag.UNKNOWN
    }

fun RoadTag.getRoadSafetyColorPair(): Pair<Int, Boolean> {
    return when (this) {
        RoadTag.CYCLE_TRACK -> android.R.color.holo_green_dark to true
        RoadTag.CYCLE_LANE,
        RoadTag.CYCLE_NETWORK,
        RoadTag.BICYCLE_DESIGNATED,
        RoadTag.BICYCLE_BOULEVARD,
        RoadTag.CCTV_CAMERA,
        RoadTag.LIT_ROUTE -> android.R.color.holo_blue_dark to true

        RoadTag.SIDE_WALK,
        RoadTag.SIDE_ROAD,
        RoadTag.SHARED_ROAD,
        RoadTag.STREET_LIGHT -> android.R.color.holo_blue_light to true

        RoadTag.MAIN_ROAD -> Color.parseColor("#ffa500") to false

        else -> android.R.color.darker_gray to true
    }
}

fun RoadTag.getRoadSafetyColor(): Int {
    return when (this) {
        RoadTag.CYCLE_LANE -> Color.parseColor("#008000")
        RoadTag.CYCLE_TRACK,
        RoadTag.CYCLE_NETWORK,
        RoadTag.BICYCLE_DESIGNATED,
        RoadTag.BICYCLE_BOULEVARD,
        RoadTag.CCTV_CAMERA,
        RoadTag.LIT_ROUTE -> Color.parseColor("#0000b3")

        RoadTag.SIDE_WALK,
        RoadTag.SIDE_ROAD,
        RoadTag.SHARED_ROAD,
        RoadTag.STREET_LIGHT -> Color.parseColor("#8080ff")

        RoadTag.MAIN_ROAD -> Color.parseColor("#ffa500")

        else -> Color.DKGRAY
    }
}

fun RoadTag.getRoadSafetyIndex(): Int {
    return when (this) {
        RoadTag.CYCLE_LANE -> 0
        RoadTag.CYCLE_TRACK,
        RoadTag.CYCLE_NETWORK,
        RoadTag.BICYCLE_DESIGNATED,
        RoadTag.BICYCLE_BOULEVARD,
        RoadTag.CCTV_CAMERA,
        RoadTag.LIT_ROUTE -> 1

        RoadTag.SIDE_WALK,
        RoadTag.SIDE_ROAD,
        RoadTag.SHARED_ROAD,
        RoadTag.STREET_LIGHT -> 2

        RoadTag.MAIN_ROAD -> 3

        else -> 4
    }
}

fun RoadTag.getTextColor(): Int {
    return when (this) {
        RoadTag.CYCLE_LANE,
        RoadTag.CYCLE_TRACK,
        RoadTag.CYCLE_NETWORK,
        RoadTag.BICYCLE_DESIGNATED,
        RoadTag.BICYCLE_BOULEVARD,
        RoadTag.CCTV_CAMERA,
        RoadTag.LIT_ROUTE,
        RoadTag.SIDE_ROAD,
            /*RoadTag.SEGREGATED,*/
        RoadTag.UNKNOWN,
        -> Color.WHITE

        else -> Color.BLACK
    }
}

fun RoadTag.getRoadTagLabel(): String {
    return when (this) {
        RoadTag.CYCLE_LANE -> "Cycle Lane"
        RoadTag.CYCLE_TRACK -> "Cycle Track"
        RoadTag.CYCLE_NETWORK -> "Cycle Network"
        RoadTag.BICYCLE_DESIGNATED -> "Designated for Cyclists"
        RoadTag.BICYCLE_BOULEVARD -> "Boulevard for Cyclists"
        RoadTag.SIDE_WALK -> "Side Walk"
        RoadTag.MAIN_ROAD -> "Main Road"
        RoadTag.SIDE_ROAD -> "Side Road"
        RoadTag.SHARED_ROAD -> "Shared Road"
        /*RoadTag.SERVICE_ROAD -> "Service Road"*/ //Ignored
        RoadTag.STREET_LIGHT -> "Street Light"
        RoadTag.CCTV_CAMERA -> "CCTV Camera"
        RoadTag.LIT_ROUTE -> "Lit Route"
        /*RoadTag.SEGREGATED -> "Segregated"*/ //Ignored
        else -> "Other"
    }
}