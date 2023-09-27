package com.skedgo.tripkit.routing

import android.graphics.Color
import androidx.annotation.IntDef
import java.lang.annotation.RetentionPolicy

enum class RoadTag(val value: String) {
    cycleLane("CYCLE-LANE"),
    cycleTrack("CYCLE-TRACK"),
    cycleNetwork("CYCLE-NETWORK"),
    bicycleDesignated("BICYCLE-DESIGNATED"),
    bicycleBoulevard("BICYCLE-BOULEVARD"),
    sideWalk("SIDE-WALK"),
    mainRoad("MAIN-ROAD"),
    sideRoad("SIDE-ROAD"),
    sharedRoad("SHARED-ROAD"),
    streetLight("STREET-LIGHT"),
    CCTVCamera("CCTV-CAMERA"),
}

fun String.getRoadSafetyColor(): Int {
    return when (this) {
        RoadTag.cycleLane.value -> RoadSafety.SAFE
        RoadTag.cycleLane.value,
        RoadTag.cycleNetwork.value,
        RoadTag.bicycleDesignated.value,
        RoadTag.bicycleBoulevard.value,
        RoadTag.CCTVCamera.value -> RoadSafety.DESIGNATED

        RoadTag.sideWalk.value,
        RoadTag.sideRoad.value,
        RoadTag.sharedRoad.value,
        RoadTag.streetLight.value -> RoadSafety.NEUTRAL

        RoadTag.mainRoad.value -> RoadSafety.HOSTILE
        else -> RoadSafety.UNKNOWN
    }
}

@IntDef(
    RoadSafety.SAFE,
    RoadSafety.DESIGNATED,
    RoadSafety.NEUTRAL,
    RoadSafety.HOSTILE,
    RoadSafety.UNKNOWN,
)
@Retention(AnnotationRetention.SOURCE)
annotation class RoadSafety {
    companion object {
        const val SAFE = Color.GREEN
        const val DESIGNATED = android.R.color.holo_blue_dark
        const val NEUTRAL = android.R.color.holo_blue_light
        const val HOSTILE = Color.YELLOW
        const val UNKNOWN = Color.GRAY
    }
}