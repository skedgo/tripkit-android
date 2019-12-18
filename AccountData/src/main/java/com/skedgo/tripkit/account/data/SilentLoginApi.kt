package com.skedgo.tripkit.account.data

import retrofit2.http.POST
import retrofit2.http.Url
import io.reactivex.Observable

internal interface SilentLoginApi {
  @POST fun logIn(@Url url: String): Observable<LogInResponse>
}
