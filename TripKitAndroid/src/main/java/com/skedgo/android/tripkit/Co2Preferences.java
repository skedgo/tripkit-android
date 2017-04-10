package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

import java.util.Map;

public interface Co2Preferences {
  /**
   * @return An immutable map having key as mode identifier for
   * which to apply emissions, and its value is emissions for
   * the supplied mode identifier in grams of CO2 per kilometre.
   */
  @NonNull Map<String, Float> getCo2Profile();

  /**
   * @param modeId        Mode identifier for which to apply these emissions.
   * @param gramsCO2PerKm Emissions for supplied mode identifier in grams of CO2 per kilometre.
   */
  void setEmissions(@NonNull String modeId, float gramsCO2PerKm);
}