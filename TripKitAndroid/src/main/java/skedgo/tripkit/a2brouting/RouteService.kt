package skedgo.tripkit.a2brouting

import com.skedgo.android.common.model.Query
import com.skedgo.android.tripkit.TransitModeFilter
import com.skedgo.android.tripkit.TransportModeFilter
import rx.Observable
import skedgo.tripkit.routing.TripGroup

interface RouteService {
  /**
   * To find routes from A to B asynchronously.
   *
   * @return An [Observable] which emits multiple of list of [TripGroup]s,
   * and then completes. That means, when we subscribe it via [Observable.subscribe],
   * it will emit the first list of [TripGroup]s. And then it will emit the second
   * list of [TripGroup]s. There may be a certain delay between the emission
   * of the first [TripGroup] list and the second [TripGroup] list due to network latency.
   *
   * By default, the returned [Observable] will operate on [rx.schedulers.Schedulers.io].
   * So be sure to [Observable.observeOn] with [rx.android.schedulers.AndroidSchedulers.mainThread]
   * to render [TripGroup]s on the main thread.
   */
  fun routeAsync(query: Query,
                 transportModeFilter: TransportModeFilter = object : TransportModeFilter {},
                 transitModeFilter: TransitModeFilter = object : TransitModeFilter {}):
      Observable<List<TripGroup>>
}