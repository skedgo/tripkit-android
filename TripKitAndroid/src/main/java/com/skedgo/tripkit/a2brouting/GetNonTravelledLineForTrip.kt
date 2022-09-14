package com.skedgo.tripkit.a2brouting

import android.graphics.Color
import com.skedgo.tripkit.common.util.PolyUtil
import io.reactivex.Observable
import com.skedgo.tripkit.routing.TripSegment
import javax.inject.Inject

class GetNonTravelledLineForTrip @Inject constructor() {

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
            it.serviceColor!!.color

          val shapes = it.shapes ?: emptyList()
          shapes to color
        }
        .flatMap { (shapes, defaultColor) ->
          shapes.filterNot { it.isTravelled }
              .filter {
                it.encodedWaypoints.isNotEmpty()
              }
              .map {
                val color = if (it.serviceColor == null || it.serviceColor.color == Color.BLACK)
                  defaultColor
                else
                  it.serviceColor.color
                PolyUtil.decode(it.encodedWaypoints)
                    .zipWithNext()
                    .map { (start, end) ->
                        com.skedgo.tripkit.LineSegment(start, end, color)
                    }
              }
        }
  }

}