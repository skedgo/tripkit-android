package skedgo.tripkit.a2brouting

import android.graphics.Color
import com.skedgo.android.common.util.PolyUtil
import com.skedgo.android.tripkit.LineSegment
import com.skedgo.android.tripkit.RegionService
import com.skedgo.android.tripkit.ServiceService
import rx.Observable
import skedgo.tripkit.routing.TripSegment
import skedgo.tripkit.routing.startDateTime
import skedgo.tripkit.routing.toSeconds
import javax.inject.Inject

class GetNonTravelledLineForTrip @Inject constructor(
    private val serviceService: ServiceService,
    private val regionService: RegionService
) {

  fun execute(segments: List<TripSegment>): Observable<List<LineSegment>> {
    return createNonTravelledLinesToDrawForStops(segments)
        .flatMap { Observable.from(it) }
  }

  private fun createNonTravelledLinesToDrawForStops(segments: List<TripSegment>?): Observable<List<List<LineSegment>>> {
    return Observable.fromCallable {
      segments.orEmpty()
          .filterNot {
            it.from == null || it.to == null || it.serviceTripId == null
          }
    }
        .flatMapIterable { it }
        .flatMap { segment ->
          regionService.getRegionByLocationAsync(segment.from)
              .map { segment to it }
              .flatMap { (segment, region) ->
                serviceService
                    .getServiceShapes(region.name!!, segment.serviceTripId, segment.startDateTime.toSeconds())
              }
              .map { shapes ->
                shapes
                    .filter {
                      it.encodedWaypoints.isNotEmpty()
                    }
                    .map {
                      PolyUtil.decode(it.encodedWaypoints)
                          .zipWithNext()
                          .map { (start, end) ->
                            LineSegment(start, end, Color.BLACK)
                          }
                    }
              }
        }
  }
}