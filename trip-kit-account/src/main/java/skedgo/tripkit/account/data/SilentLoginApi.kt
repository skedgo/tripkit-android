package skedgo.tripkit.account.data

import com.skedgo.android.tripkit.account.LogInResponse
import retrofit2.http.POST
import retrofit2.http.Url
import rx.Observable

internal interface SilentLoginApi {
  @POST fun logIn(@Url url: String): Observable<LogInResponse>
}
