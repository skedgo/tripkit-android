package com.skedgo.tripkit.account.data;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface AccountApi {
  @POST("account/signup") Observable<SignUpResponse> signUp(@Body SignUpBody body);
  @POST("account/login") Observable<LogInResponse> logIn(@Body LogInBody body);
  @POST("account/logout") Observable<LogOutResponse> logOut();
}
