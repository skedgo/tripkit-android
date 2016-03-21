package com.skedgo.android.tripkit;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.util.Collections;
import java.util.Map;

public final class DefaultCo2Preferences implements Co2Preferences {
  private final SharedPreferences preferences;

  /**
   * @param preferences This {@link SharedPreferences} should only be used to store CO2 profile.
   */
  public DefaultCo2Preferences(@NonNull SharedPreferences preferences) {
    this.preferences = preferences;
  }

  @NonNull @Override public Map<String, Float> getCo2Profile() {
    final Map<String, ?> all = preferences.getAll();
    final Map<String, Float> co2Profile = new ArrayMap<>(all.size());
    for (Map.Entry<String, ?> entry : all.entrySet()) {
      co2Profile.put(entry.getKey(), (Float) entry.getValue());
    }
    return Collections.unmodifiableMap(co2Profile);
  }

  public void setEmissions(@NonNull String modeId, float gramsCO2PerKm) {
    preferences.edit().putFloat(modeId, gramsCO2PerKm).apply();
  }
}