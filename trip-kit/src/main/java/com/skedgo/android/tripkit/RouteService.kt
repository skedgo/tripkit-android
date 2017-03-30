package com.skedgo.android.tripkit

import com.skedgo.android.common.model.Query
import com.skedgo.android.common.model.TripGroup
import rx.Observable

interface RouteService {
  @Deprecated(
      "Use GetA2bTrips instead",
      ReplaceWith("GetA2bTrips", "skedgo.tripkit.routing.a2b.GetA2bTrips"),
      DeprecationLevel.WARNING
  )
  fun routeAsync(query: Query): Observable<List<TripGroup>>
}
