package com.skedgo.android.tripkit;

import android.support.annotation.Nullable;

import java.util.Map;

public interface TripPreferences {
  /**
   * @return A map having key as mode identifier for
   * which to apply emissions, and its value is emissions for
   * the supplied mode identifier in grams of CO2 per kilometre.
   */
  @Nullable Map<String, Float> getCo2Profile();
}