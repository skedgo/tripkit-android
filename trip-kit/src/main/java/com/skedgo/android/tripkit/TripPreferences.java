package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

import java.util.Map;

public interface TripPreferences {
  /**
   * @return A immutable map having key as mode identifier for
   * which to apply emissions, and its value is emissions for
   * the supplied mode identifier in grams of CO2 per kilometre.
   */
  @NonNull Map<String, Float> getCo2Profile();
}