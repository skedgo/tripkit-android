package com.skedgo.android.accountkit.api;

import com.skedgo.android.accountkit.model.SignUpBody;
import com.skedgo.android.accountkit.model.SignUpResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface AccountApi {
  @POST("/account/signup") Observable<SignUpResponse> signUpAsync(@Body SignUpBody body);
}