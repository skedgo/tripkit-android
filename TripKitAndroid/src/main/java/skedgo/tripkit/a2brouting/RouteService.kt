package skedgo.tripkit.a2brouting

import com.skedgo.android.common.model.Query
import rx.Observable
import skedgo.tripkit.routing.TripGroup

interface RouteService {
  /**
   * To find routes from A to B.
   */
  fun routeAsync(query: Query): Observable<List<TripGroup>>
}
