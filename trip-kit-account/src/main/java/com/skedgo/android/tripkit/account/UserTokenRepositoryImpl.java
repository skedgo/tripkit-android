package com.skedgo.android.tripkit.account;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

final class UserTokenRepositoryImpl implements UserTokenRepository {
  private static final String KEY_USER_TOKEN = "userToken";
  private final SharedPreferences preferences;

  UserTokenRepositoryImpl(@NonNull SharedPreferences preferences) {
    this.preferences = preferences;
  }

  @Override public String getUserToken() {
    return preferences.getString(KEY_USER_TOKEN, null);
  }

  @Override public void putUserToken(@Nullable String userToken) {
    preferences.edit()
        .putString(KEY_USER_TOKEN, userToken)
        .apply();
  }
}
