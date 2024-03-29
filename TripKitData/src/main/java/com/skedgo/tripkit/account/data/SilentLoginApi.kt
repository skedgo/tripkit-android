package com.skedgo.tripkit.account.data

import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Url

internal interface SilentLoginApi {
  @POST fun logIn(@Url url: String): Observable<LogInResponse>
}
