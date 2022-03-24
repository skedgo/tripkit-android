package com.skedgo.tripkit;

import android.content.SharedPreferences;

import java.util.UUID;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Named;


class UuidProvider implements Callable<String> {
  private static final String KEY_UUID = "UUID";
  private final SharedPreferences preferences;

  @Inject UuidProvider(@Named("TripKitPrefs") SharedPreferences preferences) {
    this.preferences = preferences;
  }

  @Override public synchronized String call() {
    final String uuid = preferences.getString(KEY_UUID, null);
    if (uuid != null) {
      return uuid;
    } else {
      final String newUuid = UUID.randomUUID().toString();
      preferences.edit().putString(KEY_UUID, newUuid).apply();
      return newUuid;
    }
  }
}