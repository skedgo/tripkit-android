/* FIXME: Move this class to the TripKitData module. */
package com.skedgo.tripkit.analytics

import com.google.gson.JsonObject
import io.reactivex.Observable

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * See [Mark trip as planned by a user](https://skedgo.github.io/tripgo-api/#tag/Trips%2Fpaths%2F~1trip~1%7Bid%7D~1planned%2Fpost).
 */
internal interface MarkTripAsPlannedApi {
  @POST fun execute(
      @Url url: String,
      @Body userInfo: MutableMap<String, Any>
  ): Observable<JsonObject>
}
