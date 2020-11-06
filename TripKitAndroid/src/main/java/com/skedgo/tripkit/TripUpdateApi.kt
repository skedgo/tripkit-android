package com.skedgo.tripkit

import com.haroldadmin.cnradapter.NetworkResponse
import com.skedgo.tripkit.routing.RoutingResponse

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

interface TripUpdateApi {

  @GET
  fun fetchUpdateAsync(
      @Url url: String,
      @Query("includeStops") includeStops: Boolean = true,
      @Query("v") v: Int = 12
  ): Observable<RoutingResponse>

  @GET
  suspend fun fetchUpdateAsyncKt(
          @Url url: String,
          @Query("includeStops") includeStops: Boolean = true,
          @Query("v") v: Int = 12
  ): NetworkResponse<RoutingResponse, Any>

}