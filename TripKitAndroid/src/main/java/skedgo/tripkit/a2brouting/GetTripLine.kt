package skedgo.tripkit.a2brouting

import android.graphics.Color
import android.text.TextUtils
import androidx.annotation.ColorInt
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.skedgo.android.common.model.Street
import com.skedgo.android.common.model.TransportMode
import com.skedgo.android.common.model.TransportMode.*
import com.skedgo.android.common.util.PolylineEncoderUtils
import com.skedgo.android.tripkit.LineSegment
import rx.Observable
import skedgo.tripkit.routing.SegmentType
import skedgo.tripkit.routing.TripSegment
import skedgo.tripkit.routing.VehicleMode
import java.util.*
import javax.inject.Inject

// FIXME: Create a pure domain model to represent a trip line.
typealias TripLine = List<PolylineOptions>

open class GetTripLine @Inject internal constructor() {
  private val NON_TRAVELLED_LINE_COLOR: Int = Color.parseColor("#88AAAAAA")

  open fun execute(segments: List<TripSegment>): Observable<TripLine> = Observable
      .fromCallable<Pair<List<List<LineSegment>>, List<List<LineSegment>>>> {
        createLinesToDraw(segments) to createNonLinesToDraw(segments)
      }
      .map { lineSegmentPair -> createPolylineOptionsList(lineSegmentPair.first, lineSegmentPair.second) }

  private fun createPolylineOptionsList(
      results: List<List<LineSegment>>?,
      nonTravelledLinesToDraw: List<List<LineSegment>>?): TripLine {
    // Use to collect polylines that will be put onto the map
    val polylineOptionsList = LinkedList<PolylineOptions>()

    if (results != null && !results.isEmpty()) {
      val lines = LinkedList<LatLng>()
      for (list in results) {
        lines.clear()
        for (line in list) {
          lines.add(line.start)
          lines.add(line.end)
        }

        if (!lines.isEmpty()) {
          val color = list[0].color

          // If we have a non-black color, draw an outline!
          if (color != Color.BLACK) {
            polylineOptionsList.add(
                PolylineOptions()
                    .addAll(lines)
                    .color(Color.BLACK)
                    .width(10f)
            )
          }

          polylineOptionsList.add(
              PolylineOptions()
                  .addAll(lines)
                  .color(color)
                  .width((if (color != Color.BLACK) 6 else 7).toFloat())
          )
        }
      }
    }

    if (nonTravelledLinesToDraw != null && !nonTravelledLinesToDraw.isEmpty()) {
      val lines = LinkedList<LatLng>()
      for (list in nonTravelledLinesToDraw) {
        lines.clear()
        for (line in list) {
          lines.add(line.start)
          lines.add(line.end)
        }

        if (!lines.isEmpty()) {
          polylineOptionsList.add(
              PolylineOptions()
                  .addAll(lines)
                  .color(NON_TRAVELLED_LINE_COLOR)
                  .width(7f)
          )
        }
      }
    }
    return polylineOptionsList
  }

  private fun createNonLinesToDraw(segments: List<TripSegment>?): List<List<LineSegment>> {
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
          var type = LineSegment.SOLID
          if (it.mode == VehicleMode.WALK) {
            type = LineSegment.SMALL_DASH
          } else if (it.type == SegmentType.UNSCHEDULED) {
            type = LineSegment.LARGE_DASH
          }
          Triple(shapes, color, type)
        }
        .flatMap { (shapes, defaultColor, type) ->
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
                      LineSegment(start, end, type, color)
                    }
              }
        }
  }

  private fun createLinesToDraw(segments: List<TripSegment>?): List<List<LineSegment>> {
    return segments.orEmpty().filter { it.from != null && it.to != null }
        .map { segment ->
          val from = segment.from
          val to = segment.to
          var color = if (segment.serviceColor == null)
            Color.BLACK
          else
            segment.serviceColor!!.color

          val shapes = segment.shapes ?: emptyList()
          var type = LineSegment.SOLID
          if (segment.mode == VehicleMode.WALK) {
            type = LineSegment.SMALL_DASH
          } else if (segment.type == SegmentType.UNSCHEDULED) {
            type = LineSegment.LARGE_DASH
          }
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
                      LineSegment(start, end, type, color)
                    }
              }
          val lineSegmentsFromStreets = segment.streets.orEmpty()
              .filter { it.encodedWaypoints() != null }
              .flatMap { street ->
                PolylineEncoderUtils.decode(street.encodedWaypoints())
                    .zipWithNext()
                    .map { (start, end) ->
                      (getColorForWheelchairAndBicycle(street, modeId) ?: color).let {
                        LineSegment(start, end, type, it)
                      }
                    }
              }
          listOf(lineSegmentsFromShapes, lineSegmentsFromStreets,
              listOf(LineSegment(LatLng(from.lat, from.lon), LatLng(to.lat, to.lon), type, color)))
              .first {
                it.isNotEmpty()
              }
        }
  }

  @ColorInt
  fun getColorForWheelchairAndBicycle(street: Street, modeId: String?): Int? {
    return when {
      (ID_WHEEL_CHAIR == modeId || ID_BICYCLE == modeId) && street.safe() -> Color.GREEN
      ID_BICYCLE == modeId && street.dismount() -> Color.RED
      (ID_WHEEL_CHAIR == modeId || ID_BICYCLE == modeId) -> Color.YELLOW
      else -> null
    }
  }
}
