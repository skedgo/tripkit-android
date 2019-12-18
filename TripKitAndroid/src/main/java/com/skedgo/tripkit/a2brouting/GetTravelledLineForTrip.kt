package com.skedgo.tripkit.a2brouting

import android.graphics.Color
import androidx.annotation.ColorInt
import com.skedgo.android.common.model.Street
import com.skedgo.android.common.model.TransportMode
import com.skedgo.android.common.util.PolyUtil
import com.skedgo.android.common.util.TripKitLatLng
import com.skedgo.tripkit.LineSegment
import io.reactivex.Observable
import skedgo.tripkit.routing.TripSegment
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
                        com.skedgo.tripkit.LineSegment(start, end, color)
                    }
              }
          val lineSegmentsFromStreets = segment.streets.orEmpty()
              .filter { it.encodedWaypoints() != null }
              .flatMap { street ->
                PolyUtil.decode(street.encodedWaypoints())
                    .zipWithNext()
                    .map { (start, end) ->
                      (getColorForWheelchairAndBicycle(street, modeId) ?: color).let {
                          com.skedgo.tripkit.LineSegment(start, end, it)
                      }
                    }
              }
          listOf(lineSegmentsFromShapes, lineSegmentsFromStreets,
              listOf(com.skedgo.tripkit.LineSegment(TripKitLatLng(from.lat, from.lon), TripKitLatLng(to.lat, to.lon), color)))
              .first {
                it.isNotEmpty()
              }
        }
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