package com.skedgo.android.tripkit.account;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface AccountApi {
  @POST("account/signup") Observable<SignUpResponse> signUpAsync(@Body SignUpBody body);
  @POST("account/login") Observable<LogInResponse> logInAsync(@Body LogInBody body);
  @POST("account/logout") Observable<LogOutResponse> logOutAsync();
}
