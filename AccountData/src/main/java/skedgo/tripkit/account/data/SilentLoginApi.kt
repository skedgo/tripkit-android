package skedgo.tripkit.account.data

import retrofit2.http.POST
import retrofit2.http.Url
import rx.Observable

internal interface SilentLoginApi {
  @POST fun logIn(@Url url: String): Observable<LogInResponse>
}
