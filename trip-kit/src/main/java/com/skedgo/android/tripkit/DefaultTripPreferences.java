package com.skedgo.android.tripkit;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public final class DefaultTripPreferences implements TripPreferences {
  private final SharedPreferences preferences;

  public DefaultTripPreferences(@NonNull SharedPreferences preferences) {
    this.preferences = preferences;
  }

  @Override public boolean isConcessionPricingPreferred() {
    return preferences.getBoolean("isConcessionPricingPreferred", false);
  }

  @Override public void setConcessionPricingPreferred(boolean isConcessionPricingPreferred) {
    preferences.edit()
        .putBoolean("isConcessionPricingPreferred", isConcessionPricingPreferred)
        .apply();
  }
}