package com.skedgo.android.tripkit.account;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

public class AccountService {
  private static final String KEY_USERNAME = "username";
  private final AccountApi api;
  private final UserTokenRepository userTokenRepository;
  private final SharedPreferences preferences;

  public AccountService(
      AccountApi api,
      UserTokenRepository userTokenRepository,
      SharedPreferences preferences) {
    this.api = api;
    this.userTokenRepository = userTokenRepository;
    this.preferences = preferences;
  }

  public Observable<SignUpResponse> signUpAsync(final SignUpBody body) {
    return api.signUpAsync(body)
        .doOnNext(new Action1<SignUpResponse>() {
          @Override public void call(SignUpResponse response) {
            userTokenRepository.putUserToken(response.userToken());
            preferences.edit().putString(KEY_USERNAME, body.username()).apply();
          }
        });
  }

  public Observable<LogInResponse> logInAsync(final LogInBody body) {
    return api.logInAsync(body)
        .doOnNext(new Action1<LogInResponse>() {
          @Override public void call(LogInResponse response) {
            userTokenRepository.putUserToken(response.userToken());
            preferences.edit().putString(KEY_USERNAME, body.username()).apply();
          }
        });
  }

  public Observable<LogOutResponse> logOutAsync() {
    return api.logOutAsync()
        .doOnCompleted(new Action0() {
          @Override public void call() {
            userTokenRepository.putUserToken(null);
            preferences.edit().clear().apply();
          }
        });
  }

  public Observable<LogInResponse> logInSilentAsync(String token) {
    return api.logInSilentAsync("account/android/" + token)
        .doOnNext(new Action1<LogInResponse>() {
          @Override public void call(LogInResponse response) {
            userTokenRepository.putUserToken(response.userToken());
          }
        });
  }

  /**
   * @return True if users already logged in. Otherwise, false.
   */
  public boolean hasUser() {
    return userTokenRepository.getUserToken() != null;
  }

  /**
   * @return If {@link #hasUser()} returns true,
   * this returns the username that users used
   * to log in or to sign up before.
   */
  @Nullable public String username() {
    return preferences.getString(KEY_USERNAME, null);
  }
}