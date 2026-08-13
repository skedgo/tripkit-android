package com.skedgo.tripkit.a2brouting

import android.graphics.Color
import com.google.maps.android.PolyUtil
import com.google.maps.android.ktx.utils.simplify
import com.skedgo.tripkit.common.util.TripKitLatLng
import com.skedgo.tripkit.routing.TripSegment
import io.reactivex.Observable
import javax.inject.Inject

class GetNonTravelledLineForTrip @Inject constructor() {

    companion object {
        const val LAT_LNG_SIMPLIFY_TOLERANCE = 5.0
    }

    fun execute(segments: List<TripSegment>): Observable<List<com.skedgo.tripkit.LineSegment>> {
        return Observable.fromCallable { createNonTravelledLinesToDraw(segments) }
            .flatMap { Observable.fromIterable(it) }
    }

    private fun createNonTravelledLinesToDraw(segments: List<TripSegment>?): List<List<com.skedgo.tripkit.LineSegment>> {
        return segments.orEmpty()
            .filterNot {
                it.from == null || it.to == null
            }
            .flatMap { segment ->
                val defaultColor = if (segment.serviceColor == null)
                    Color.BLACK
                else
                    segment.serviceColor?.color ?: Color.BLACK

                segment.shapes.orEmpty()
                    .filterNot { it.isTravelled }
                    .filter {
                        it.encodedWaypoints.isNotEmpty()
                    }
                    .map { shape ->
                        val lineColor =
                            if (shape.serviceColor == null || shape.serviceColor.color == Color.BLACK)
                                defaultColor
                            else
                                shape.serviceColor.color
                        PolyUtil.decode(shape.encodedWaypoints).simplify(LAT_LNG_SIMPLIFY_TOLERANCE)
                            .zipWithNext()
                            .map { (start, end) ->
                                com.skedgo.tripkit.LineSegment(
                                    TripKitLatLng(start.latitude, start.longitude),
                                    TripKitLatLng(end.latitude, end.longitude),
                                    lineColor,
                                    "",
                                    segment.trip?.uuid,
                                    segment.segmentId
                                )
                            }
                    }
            }
    }

}
