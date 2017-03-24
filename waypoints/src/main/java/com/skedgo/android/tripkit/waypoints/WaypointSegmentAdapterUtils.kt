package com.skedgo.android.tripkit.waypoints


import android.support.annotation.VisibleForTesting
import com.skedgo.android.common.model.Location
import com.skedgo.android.common.model.Region
import com.skedgo.android.common.model.SegmentType
import com.skedgo.android.common.model.TripSegment
import com.skedgo.android.tripkit.TimetableEntry
import java.util.*
import javax.inject.Inject

class WaypointSegmentAdapterUtils
@Inject internal constructor() {

    @VisibleForTesting
    internal fun adaptServiceSegmentList(prototypeSegment: TripSegment,
                                         service: TimetableEntry,
                                         region: Region,
                                         segments: List<TripSegment>): List<WaypointSegmentAdapter> {

        val waypointSegments = ArrayList<WaypointSegmentAdapter>(segments.size)

        for (segment in segments) {

            when {
                (segment.id == prototypeSegment.id) ->
                    waypointSegments.add(WaypointSegmentAdapter(
                            start = service.stopCode,
                            end = service.endStopCode,
                            modes = listOf("pt_pub", "pt_sch"),
                            startTime = service.startTimeInSecs.toInt(),
                            endTime = service.endTimeInSecs.toInt(),
                            operator = service.operator, region = region.name!!))

                (segment.type !in setOf(SegmentType.STATIONARY, SegmentType.ARRIVAL, SegmentType.DEPARTURE)) ->
                    waypointSegments.add(WaypointSegmentAdapter(
                            start = segment.from.coordinateString,
                            end = segment.to.coordinateString,
                            modes = listOf(segment.transportModeId!!)))
            }
        }

        return waypointSegments
    }

    @VisibleForTesting internal fun adaptStopSegmentList(prototypeSegment: TripSegment,
                                                         waypoint: Location, isGetOn: Boolean,
                                                         segments: List<TripSegment>): List<WaypointSegmentAdapter> {
        val waypointSegments = ArrayList<WaypointSegmentAdapter>(segments.size)

        var changeNextDeparture = false
        var isTimeAdded = false

        for (i in segments.indices) {

            val segment = segments[i]

            when {
                (segment.id == prototypeSegment.id) -> {
                    changeNextDeparture = !isGetOn

                    waypointSegments.add(WaypointSegmentAdapter(
                            start =
                            if (isGetOn) waypoint.coordinateString
                            else segment.from.coordinateString,
                            end =
                            if (isGetOn) segment.to.coordinateString
                            else waypoint.coordinateString,
                            modes = listOf(segment.transportModeId!!),
                            startTime = if (!isTimeAdded) segment.startTimeInSecs.toInt() else 0

                    ))
                }

                (segment.type !in setOf(SegmentType.STATIONARY, SegmentType.ARRIVAL, SegmentType.DEPARTURE)) ->

                    waypointSegments.add(WaypointSegmentAdapter(
                            start =
                            if (changeNextDeparture) {
                                changeNextDeparture = false
                                waypoint.coordinateString
                            } else segment.from.coordinateString,
                            // When is get on, check if next segment is the one that changes, meaning "end" changes here
                            end = if (isGetOn && i < segments.size - 1 && segments[i + 1].id == prototypeSegment.id) {
                                waypoint.coordinateString
                            } else {
                                segment.to.coordinateString
                            }
                            ,
                            modes = listOf(segment.transportModeId!!),
                            startTime = if (!isTimeAdded) {
                                // We only add once.
                                isTimeAdded = true
                                segment.startTimeInSecs.toInt()
                            } else 0

                    ))
            }
        }

        return waypointSegments
    }
}
