package com.skedgo.android.tripkit

import android.graphics.Color
import android.text.TextUtils
import android.util.Pair
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.skedgo.android.common.model.SegmentType
import com.skedgo.android.common.model.TransportMode
import com.skedgo.android.common.model.TripSegment
import com.skedgo.android.common.model.VehicleMode
import com.skedgo.android.common.util.PolylineEncoderUtils
import rx.Observable
import java.util.*
import javax.inject.Inject

typealias TripLine = List<PolylineOptions>

class GetTripLine @Inject constructor() {
  private val NON_TRAVELLED_LINE_COLOR = 0x88AAAAAA.toInt()

  fun execute(segments: List<TripSegment>): Observable<TripLine>
      = Observable
      .fromCallable<Pair<List<List<LineSegment>>, List<List<LineSegment>>>> { createLinesToDraw(segments) }
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

  private fun createLinesToDraw(segments: List<TripSegment>?): Pair<List<List<LineSegment>>, List<List<LineSegment>>> {
    val linesToDraw = LinkedList<List<LineSegment>>()
    val nonTravelledLinesToDraw = LinkedList<List<LineSegment>>()
    if (segments == null) {
      return Pair(linesToDraw, nonTravelledLinesToDraw)
    }

    for (segment in segments) {
      val from = segment.from
      val to = segment.to

      if (from == null || to == null) {
        continue
      }

      var color = if (segment.serviceColor == null)
        Color.BLACK
      else
        segment.serviceColor!!.color

      var hasAddedLines = false

      if (segment.shapes != null && !segment.shapes.isEmpty()) {
        for (shape in segment.shapes) {
          color = if (shape.serviceColor == null || shape.serviceColor.color == Color.BLACK)
            color
          else
            shape.serviceColor.color

          val lineSegmentsToDraw = LinkedList<LineSegment>()

          var wps: List<LatLng>? = null
          if (!TextUtils.isEmpty(shape.encodedWaypoints)) {
            wps = PolylineEncoderUtils.decode(shape.encodedWaypoints)
          }

          val nonTravelledLines = ArrayList<LineSegment>()
          if (wps != null && !wps.isEmpty()) {
            var j = 0
            val size = wps.size - 1
            while (j < size) {
              val start = wps[j]
              val end = wps[j + 1]

              var type = LineSegment.SOLID
              if (segment.mode == VehicleMode.WALK) {
                type = LineSegment.SMALL_DASH
              } else if (segment.type == SegmentType.UNSCHEDULED) {
                type = LineSegment.LARGE_DASH
              }

              if (shape.isTravelled) {
                if (!nonTravelledLines.isEmpty()) {
                  nonTravelledLinesToDraw.add(ArrayList(nonTravelledLines))
                  nonTravelledLines.clear()
                }

                lineSegmentsToDraw.add(LineSegment(start, end, type, color))
              } else {
                nonTravelledLines.add(LineSegment(start, end, type, color))
              }
              j++
            }

            hasAddedLines = true
          }

          linesToDraw.add(lineSegmentsToDraw)

          if (!nonTravelledLines.isEmpty()) {
            nonTravelledLinesToDraw.add(ArrayList(nonTravelledLines))
            nonTravelledLines.clear()
          }
        }
      } else if (segment.streets != null && !segment.streets.isEmpty()) {
        for (street in segment.streets) {
          val lineSegmentsToDraw = LinkedList<LineSegment>()
          var wps: List<LatLng>? = null
          if (!TextUtils.isEmpty(street.encodedWaypoints())) {
            wps = PolylineEncoderUtils.decode(street.encodedWaypoints())
          }

          if (wps != null && !wps.isEmpty()) {
            var j = 0
            val size = wps.size - 1
            while (j < size) {
              val start = wps[j]
              val end = wps[j + 1]

              var type = LineSegment.SOLID
              if (segment.mode == VehicleMode.WALK) {
                type = LineSegment.SMALL_DASH
              } else if (segment.type == SegmentType.UNSCHEDULED) {
                type = LineSegment.LARGE_DASH
              }

              val modeId = segment.transportModeId
              if (TransportMode.ID_WHEEL_CHAIR == modeId || TransportMode.ID_BICYCLE == modeId) {
                color = if (street.safe()) Color.GREEN else Color.YELLOW
              }

              lineSegmentsToDraw.add(LineSegment(start, end, type, color))
              j++
            }

            hasAddedLines = true
          }

          linesToDraw.add(lineSegmentsToDraw)
        }
      }

      if (!hasAddedLines) {
        // We don't have any specific way points, lets check for a 'To' location.
        val start = LatLng(from.lat, from.lon)
        val end = LatLng(to.lat, to.lon)

        var type = LineSegment.SOLID
        if (segment.mode == VehicleMode.WALK) {
          type = LineSegment.SMALL_DASH
        } else if (segment.type == SegmentType.UNSCHEDULED) {
          type = LineSegment.LARGE_DASH
        }

        linesToDraw.add(listOf(LineSegment(start, end, type, color)))
      }
    }

    return Pair(linesToDraw, nonTravelledLinesToDraw)
  }
}
