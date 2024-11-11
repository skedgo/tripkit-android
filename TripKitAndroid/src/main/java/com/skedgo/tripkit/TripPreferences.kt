package com.skedgo.tripkit;

import com.skedgo.tripkit.data.tsp.RegionInfo;

import io.reactivex.Observable;

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

    /**
     * @return An {@link Observable} which emits value of {@link #isWheelchairPreferred()}
     * when it has changed.
     */
    Observable<Boolean> whenWheelchairPreferenceChanges();
}
