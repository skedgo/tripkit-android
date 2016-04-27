package com.skedgo.android.tripkit.account.api;

import com.skedgo.android.tripkit.account.model.LogInBody;
import com.skedgo.android.tripkit.account.model.LogInResponse;
import com.skedgo.android.tripkit.account.model.LogOutResponse;
import com.skedgo.android.tripkit.account.model.SignUpBody;
import com.skedgo.android.tripkit.account.model.SignUpResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface AccountApi {
  @POST("/account/signup") Observable<SignUpResponse> signUpAsync(
      @Body SignUpBody body
  );
  @POST("/account/login") Observable<LogInResponse> logInAsync(
      @Body LogInBody body
  );
  @POST("/account/logout") Observable<LogOutResponse> logOutAsync();
}