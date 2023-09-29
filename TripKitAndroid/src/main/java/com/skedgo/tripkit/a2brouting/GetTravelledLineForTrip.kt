package com.skedgo.tripkit.a2brouting

import android.graphics.Color
import androidx.annotation.ColorInt
import com.skedgo.tripkit.common.model.Street
import com.skedgo.tripkit.common.model.TransportMode
import com.skedgo.tripkit.common.util.PolyUtil
import com.skedgo.tripkit.common.util.TripKitLatLng
import com.skedgo.tripkit.routing.RoadTag
import io.reactivex.Observable
import com.skedgo.tripkit.routing.TripSegment
import com.skedgo.tripkit.routing.getRoadSafetyColor
import java.lang.Exception
import javax.inject.Inject

class GetTravelledLineForTrip @Inject constructor() {

    fun execute(segments: List<TripSegment>?): Observable<List<com.skedgo.tripkit.LineSegment>> {
        return Observable
            .fromCallable {
                createTravelledLinesToDraw(segments)
            }
            .flatMap { Observable.fromIterable(it) }
    }

    private fun createTravelledLinesToDraw(segments: List<TripSegment>?): List<List<com.skedgo.tripkit.LineSegment>> {
        return segments.orEmpty().filter { it.from != null && it.to != null }
            .map { segment ->
                val from = segment.from
                val to = segment.to
                var color = if (segment.serviceColor == null)
                    Color.BLACK
                else
                    segment.serviceColor!!.color

                val shapes = segment.shapes ?: emptyList()
                val modeId = segment.transportModeId
                val lineSegmentsFromShapes = shapes.filter { it.isTravelled }
                    .flatMap {
                        color = if (it.serviceColor == null || it.serviceColor.color == Color.BLACK)
                            color
                        else
                            it.serviceColor.color
                        PolyUtil.decode(it.encodedWaypoints)
                            .orEmpty().zipWithNext()
                            .map { (start, end) ->
                                com.skedgo.tripkit.LineSegment(
                                    start, end, color,
                                    com.skedgo.tripkit.LineSegment.Tag.SHAPE.toString()
                                )
                            }
                    }
                val lineSegmentsFromStreets = segment.streets.orEmpty()
                    .filter { it.encodedWaypoints() != null }
                    .flatMap { street ->
                        PolyUtil.decode(street.encodedWaypoints())
                            .zipWithNext()
                            .map { (start, end) ->
                                var lineColor = (getColorForWheelchairAndBicycle(street, modeId) ?: color)
                                if(!street.roadTags().isNullOrEmpty()) {
                                    lineColor = Color.BLUE
                                }
                                com.skedgo.tripkit.LineSegment(
                                    start, end, lineColor,
                                    com.skedgo.tripkit.LineSegment.Tag.STREET.toString()
                                )
                            }
                    }
                listOf(
                    lineSegmentsFromShapes, lineSegmentsFromStreets,
                    listOf(
                        com.skedgo.tripkit.LineSegment(
                            TripKitLatLng(from.lat, from.lon),
                            TripKitLatLng(to.lat, to.lon),
                            color,
                            ""
                        )
                    )
                )
                    .first {
                        it.isNotEmpty()
                    }
            }
    }

    private fun String.getRoadTagColor(): Int {
        val roadTag = try {
            RoadTag.valueOf(this.replace("-","_"))
        } catch (e: Exception) {
            RoadTag.UNKNOWN
        }

        return roadTag.getRoadSafetyColor()
    }

    @ColorInt
    fun getColorForWheelchairAndBicycle(street: Street, modeId: String?): Int? {
        return when {
            (TransportMode.ID_WHEEL_CHAIR == modeId || TransportMode.ID_BICYCLE == modeId) && street.safe() -> Color.GREEN
            TransportMode.ID_BICYCLE == modeId && street.dismount() -> Color.RED
            (TransportMode.ID_WHEEL_CHAIR == modeId || TransportMode.ID_BICYCLE == modeId) -> Color.YELLOW
            else -> null
        }
    }

}