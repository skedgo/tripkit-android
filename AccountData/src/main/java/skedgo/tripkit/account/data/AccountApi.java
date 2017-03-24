package skedgo.tripkit.account.data;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

interface AccountApi {
  @POST("account/signup") Observable<SignUpResponse> signUp(@Body SignUpBody body);
  @POST("account/login") Observable<LogInResponse> logIn(@Body LogInBody body);
  @POST("account/logout") Observable<LogOutResponse> logOut();
}
