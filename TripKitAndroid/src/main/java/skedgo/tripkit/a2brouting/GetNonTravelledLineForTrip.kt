package skedgo.tripkit.a2brouting

import android.graphics.Color
import androidx.annotation.ColorInt
import com.google.maps.android.PolyUtil
import com.skedgo.android.common.model.Street
import com.skedgo.android.common.model.TransportMode
import com.skedgo.android.common.util.PolylineEncoderUtils
import com.skedgo.android.tripkit.LineSegment
import rx.Observable
import rx.Single
import skedgo.tripkit.routing.TripSegment
import javax.inject.Inject

class GetNonTravelledLineForTrip @Inject constructor() {

  fun execute(segments: List<TripSegment>): Observable<List<LineSegment>> {
    return Observable.fromCallable { createNonTravelledLinesToDraw(segments) }
        .flatMap { Observable.from(it) }
  }

  private fun createNonTravelledLinesToDraw(segments: List<TripSegment>?): List<List<LineSegment>> {
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
                PolylineEncoderUtils.decode(it.encodedWaypoints)
                    .zipWithNext()
                    .map { (start, end) ->
                      LineSegment(start, end, color)
                    }
              }
        }
  }

}