package com.skedgo.android.tripkit

import com.skedgo.android.common.model.Query
import com.skedgo.android.common.model.TripGroup
import rx.Observable

interface RouteService {
  @Deprecated(
      "Use GetA2bRoutingResults instead",
      ReplaceWith("GetA2bRoutingResults", "skedgo.tripkit.a2brouting.GetA2bRoutingResults"),
      DeprecationLevel.WARNING
  )
  fun routeAsync(query: Query): Observable<List<TripGroup>>
}
