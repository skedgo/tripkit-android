package skedgo.validbookingcountcore

import retrofit2.http.GET
import rx.Observable

interface ValidBookingCountApi {
  @GET("booking/valid/count")
  fun fetchValidBookingsCountAsync(): Observable<ValidBookingCountResponse>
}
