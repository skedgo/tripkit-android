package skedgo.tripkit.timetable

import com.skedgo.android.common.model.DeparturesResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import rx.Single

interface DeparturesApi {
  @POST
  fun request(@Url url: String, @Body departureRequestBody: DepartureRequestBody): Single<DeparturesResponse>
}