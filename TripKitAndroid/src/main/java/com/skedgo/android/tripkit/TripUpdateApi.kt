package com.skedgo.android.tripkit

import skedgo.tripkit.routing.RoutingResponse

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import rx.Observable

internal interface TripUpdateApi {

  @GET
  fun fetchUpdateAsync(
      @Url url: String,
      @Query("includeStops") includeStops: Boolean = true,
      @Query("v") v: Int = 12
  ): Observable<RoutingResponse>
}