package com.skedgo.tripkit.a2brouting

import android.graphics.Color
import androidx.annotation.ColorInt
import com.google.maps.android.PolyUtil
import com.google.maps.android.ktx.utils.simplify
import com.skedgo.tripkit.common.model.Street
import com.skedgo.tripkit.common.model.TransportMode
import com.skedgo.tripkit.common.util.TripKitLatLng
import com.skedgo.tripkit.routing.RoadTag
import io.reactivex.Observable
import com.skedgo.tripkit.routing.TripSegment
import com.skedgo.tripkit.routing.getRoadSafetyColor
import java.lang.Exception
import javax.inject.Inject

class GetTravelledLineForTrip @Inject constructor() {

    companion object {
        const val LAT_LNG_SIMPLIFY_TOLERANCE = 5.0
    }

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
                        val decodedWayPoints = PolyUtil.decode(it.encodedWaypoints)
                        val simplified = decodedWayPoints.simplify(LAT_LNG_SIMPLIFY_TOLERANCE)
                        simplified.zipWithNext()
                            .map { (start, end) ->
                                com.skedgo.tripkit.LineSegment(
                                    TripKitLatLng(start.latitude, start.longitude),
                                    TripKitLatLng(end.latitude, end.longitude),
                                    color,
                                    com.skedgo.tripkit.LineSegment.Tag.SHAPE.toString()
                                )
                            }
                    }
                val lineSegmentsFromStreets = segment.streets.orEmpty()
                    .filter { it.encodedWaypoints() != null }
                    .flatMap { street ->
                        PolyUtil.decode(street.encodedWaypoints()).simplify(
                            LAT_LNG_SIMPLIFY_TOLERANCE
                        )
                            .zipWithNext()
                            .map { (start, end) ->
                                var lineColor =
                                    (getColorForWheelchairAndBicycle(street, modeId) ?: color)
                                street.roadTags()?.let {
                                    it.firstOrNull()?.let {
                                        lineColor = it.getRoadTagColor()
                                    }
                                }
                                com.skedgo.tripkit.LineSegment(
                                    TripKitLatLng(start.latitude, start.longitude),
                                    TripKitLatLng(end.latitude, end.longitude),
                                    lineColor,
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