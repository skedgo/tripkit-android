package skedgo.tripkit.a2brouting

import com.skedgo.android.common.model.Location
import com.skedgo.android.common.model.Query
import com.skedgo.android.common.model.TimeTag
import com.skedgo.android.tripkit.RouteService
import rx.Observable
import skedgo.tripkit.a2brouting.RequestTime.*
import skedgo.tripkit.routing.TripGroup
import skedgo.tripkit.routing.toSeconds
import javax.inject.Inject

open class GetA2bRoutingResults @Inject internal constructor(
    private val routeService: RouteService
) {
  open fun execute(request: A2bRoutingRequest): Observable<List<TripGroup>>
      = Observable
      .fromCallable {
        val origin = Location(request.origin.first, request.origin.second)
        origin.address = request.originAddress

        val destination = Location(request.destination.first, request.destination.second)
        destination.address = request.destinationAddress

        val query = Query()
        query.fromLocation = origin
        query.toLocation = destination
        val time = request.time
        when (time) {
          is DepartAfter -> query.setTimeTag(TimeTag.createForLeaveAfter(time.dateTime.toSeconds()))
          is ArriveBefore -> query.setTimeTag(TimeTag.createForArriveBy(time.dateTime.toSeconds()))
          is DepartNow -> query.setTimeTag(TimeTag.createForLeaveAfter(time.getNow().toSeconds()))
        }
        query
      }
      .flatMap { routeService.routeAsync(it) }
}