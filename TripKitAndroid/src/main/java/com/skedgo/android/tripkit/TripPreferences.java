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
   * @return An {@link Observable} which emits value of {@link #isConcessionPricingPreferred()}
   * when it has changed.
   */
  Observable<Boolean> whenConcessionPricingPreferenceChanges();

  /**
   * This option should be used when {@link RegionInfo#transitWheelchairAccessibility()} is true.
   */
  boolean isWheelchairPreferred();
  void setWheelchairPreferred(boolean isWheelchairPreferred);

  boolean isWheelchairPreferredOnBoarding();
  void setWheelchairPreferredOnBoarding(boolean isWheelchairPreferred);

  /**
   * @return An {@link Observable} which emits value of {@link #isWheelchairPreferred()}
   * when it has changed.
   */
  Observable<Boolean> whenWheelchairPreferenceChanges();
}
