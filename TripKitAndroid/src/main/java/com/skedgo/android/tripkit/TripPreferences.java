package com.skedgo.android.tripkit;

import com.skedgo.android.tripkit.tsp.RegionInfo;

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
  void setWheelchairPreferred(boolean isWheelchairPreferred);
}