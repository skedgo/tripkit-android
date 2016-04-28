package com.skedgo.android.tripkit.account.api;

import android.support.annotation.NonNull;

import com.skedgo.android.tripkit.account.model.LogInBody;
import com.skedgo.android.tripkit.account.model.LogInResponse;
import com.skedgo.android.tripkit.account.model.LogOutResponse;
import com.skedgo.android.tripkit.account.model.SignUpBody;
import com.skedgo.android.tripkit.account.model.SignUpResponse;

import retrofit2.http.Body;
import rx.Observable;
import rx.functions.Action1;

final class AccountServiceImpl implements AccountService {
  private final AccountApi api;
  private final UserTokenStore tokenStore;

  AccountServiceImpl(
      @NonNull AccountApi api,
      @NonNull UserTokenStore tokenStore) {
    this.api = api;
    this.tokenStore = tokenStore;
  }

  @Override public Observable<SignUpResponse> signUpAsync(@Body SignUpBody body) {
    return api.signUpAsync(body)
        .doOnNext(new Action1<SignUpResponse>() {
          @Override public void call(SignUpResponse response) {
            tokenStore.put(response.userToken());
          }
        });
  }

  @Override public Observable<LogInResponse> logInAsync(@Body LogInBody body) {
    return api.logInAsync(body)
        .doOnNext(new Action1<LogInResponse>() {
          @Override public void call(LogInResponse response) {
            tokenStore.put(response.userToken());
          }
        });
  }

  @Override public Observable<LogOutResponse> logOutAsync() {
    return api.logOutAsync();
  }
}