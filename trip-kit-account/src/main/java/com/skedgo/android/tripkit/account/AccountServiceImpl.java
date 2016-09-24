package com.skedgo.android.tripkit.account;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import retrofit2.http.Body;
import retrofit2.http.Url;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

final class AccountServiceImpl implements AccountService {
  private static final String KEY_USERNAME = "username";
  private final AccountApi api;
  private final UserTokenStore tokenStore;
  private final SharedPreferences preferences;

  AccountServiceImpl(
      @NonNull AccountApi api,
      @NonNull UserTokenStore tokenStore,
      @NonNull SharedPreferences preferences) {
    this.api = api;
    this.tokenStore = tokenStore;
    this.preferences = preferences;
  }

  @Override public Observable<SignUpResponse> signUpAsync(@Body final SignUpBody body) {
    return api.signUpAsync(body)
        .doOnNext(new Action1<SignUpResponse>() {
          @Override public void call(SignUpResponse response) {
            tokenStore.put(response.userToken());
            preferences.edit().putString(KEY_USERNAME, body.username()).apply();
          }
        });
  }

  @Override public Observable<LogInResponse> logInAsync(@Body final LogInBody body) {
    return api.logInAsync(body)
        .doOnNext(new Action1<LogInResponse>() {
          @Override public void call(LogInResponse response) {
            tokenStore.put(response.userToken());
            preferences.edit().putString(KEY_USERNAME, body.username()).apply();
          }
        });
  }

  @Override public Observable<LogOutResponse> logOutAsync() {
    return api.logOutAsync()
        .doOnCompleted(new Action0() {
          @Override public void call() {
            tokenStore.put(null);
            preferences.edit().clear().apply();
          }
        });
  }

  @Override public Observable<LogInResponse> logInSilentAsync(@Url String token) {
    return api.logInSilentAsync("/account/android/" + token)
        .doOnNext(new Action1<LogInResponse>() {
          @Override public void call(LogInResponse response) {
            tokenStore.put(response.userToken());
          }
        });
  }

  @Override public boolean hasUser() {
    return tokenStore.call() != null;
  }

  @Nullable @Override public String username() {
    return preferences.getString(KEY_USERNAME, null);
  }
}