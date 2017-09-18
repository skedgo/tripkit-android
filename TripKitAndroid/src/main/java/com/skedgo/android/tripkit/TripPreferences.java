package com.skedgo.android.tripkit;

import com.skedgo.android.tripkit.tsp.RegionInfo;

import rx.Observable;

public interface TripPreferences {
  /**
   * This option should be used when {@link RegionInfo#supportsConcessionPricing()} is true.
   */
  boolean isConcessionPricingPreferred();
  void setConcessionPricingPreferred(boolean isConcessionPricingPreferred);

  /**
   * This option should be used when {@link RegionInfo#transitWheelchairAccessibility()} is true.
   */
  boolean isWheelchairPreferred();
  Observable<Boolean> whenWheelchairPreferenceChanges();
  void setWheelchairPreferred(boolean isWheelchairPreferred);
}