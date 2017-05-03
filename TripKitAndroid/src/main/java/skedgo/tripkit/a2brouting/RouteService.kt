package skedgo.tripkit.a2brouting

import com.skedgo.android.common.model.Query
import rx.Observable
import skedgo.tripkit.routing.TripGroup

@Deprecated("Use GetA2bRoutingResults instead")
interface RouteService {
  @Deprecated("Use GetA2bRoutingResults instead")
  fun routeAsync(query: Query): Observable<List<TripGroup>>
}
