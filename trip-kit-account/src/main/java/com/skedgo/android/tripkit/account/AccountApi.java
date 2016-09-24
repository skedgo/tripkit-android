package com.skedgo.android.tripkit.account;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @see <a href="http://planck.buzzhives.com/tripgodata/account/resource_AccountSpecificRestService.html">TripGo Account API</a>
 */
public interface AccountApi {
  @POST("account/signup") Observable<SignUpResponse> signUpAsync(@Body SignUpBody body);
  @POST("account/login") Observable<LogInResponse> logInAsync(@Body LogInBody body);
  @POST("account/logout") Observable<LogOutResponse> logOutAsync();
  @POST() Observable<LogInResponse> logInSilentAsync(@Url String token);
}