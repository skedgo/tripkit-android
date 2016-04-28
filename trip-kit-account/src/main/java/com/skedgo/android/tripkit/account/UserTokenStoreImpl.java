package com.skedgo.android.tripkit.account;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

final class UserTokenStoreImpl extends UserTokenStore {
  private static final String KEY_USER_TOKEN = "userToken";
  private final SharedPreferences preferences;

  UserTokenStoreImpl(@NonNull SharedPreferences preferences) {
    this.preferences = preferences;
  }

  @Override public String call() {
    return preferences.getString(KEY_USER_TOKEN, null);
  }

  @Override void put(@Nullable String userToken) {
    preferences.edit()
        .putString(KEY_USER_TOKEN, userToken)
        .apply();
  }
}