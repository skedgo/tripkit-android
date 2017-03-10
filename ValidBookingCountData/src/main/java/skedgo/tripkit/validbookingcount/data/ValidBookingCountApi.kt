package skedgo.tripkit.validbookingcount.data

import retrofit2.http.GET
import rx.Observable

internal interface ValidBookingCountApi {
  @GET("booking/valid/count") fun fetchValidBookingCount(): Observable<ValidBookingCountResponse>
}
