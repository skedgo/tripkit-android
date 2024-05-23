package com.skedgo.tripkit.a2brouting

import android.graphics.Color
import com.google.maps.android.PolyUtil
import com.google.maps.android.ktx.utils.simplify
import com.skedgo.tripkit.common.util.TripKitLatLng
import io.reactivex.Observable
import com.skedgo.tripkit.routing.TripSegment
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
            .map {
                val color = if (it.serviceColor == null)
                    Color.BLACK
                else
                    it.serviceColor?.color ?: Color.BLACK

                val shapes = it.shapes ?: emptyList()
                shapes to color
            }
            .flatMap { (shapes, defaultColor) ->
                shapes.filterNot { it.isTravelled }
                    .filter {
                        it.encodedWaypoints.isNotEmpty()
                    }
                    .map {
                        val color =
                            if (it.serviceColor == null || it.serviceColor.color == Color.BLACK)
                                defaultColor
                            else
                                it.serviceColor.color
                        PolyUtil.decode(it.encodedWaypoints).simplify(LAT_LNG_SIMPLIFY_TOLERANCE)
                            .zipWithNext()
                            .map { (start, end) ->
                                com.skedgo.tripkit.LineSegment(
                                    TripKitLatLng(start.latitude, start.longitude),
                                    TripKitLatLng(end.latitude, end.longitude),
                                    color,
                                    ""
                                )
                            }
                    }
            }
    }

}