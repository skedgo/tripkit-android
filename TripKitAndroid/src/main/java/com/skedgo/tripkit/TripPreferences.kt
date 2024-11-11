package com.skedgo.tripkit

import io.reactivex.Observable

interface TripPreferences {

    /**
     * This option should be used when [RegionInfo.supportsConcessionPricing] is true.
     */
    fun isConcessionPricingPreferred(): Boolean

    fun setConcessionPricingPreferred(isConcessionPricingPreferred: Boolean)

    /**
     * An [Observable] which emits the value of [isConcessionPricingPreferred] when it changes.
     */
    fun whenConcessionPricingPreferenceChanges(): Observable<Boolean>

    /**
     * This option should be used when [RegionInfo.transitWheelchairAccessibility] is true.
     */
    fun isWheelchairPreferred(): Boolean

    fun setWheelchairPreferred(isWheelchairPreferred: Boolean)

    /**
     * An [Observable] which emits the value of [isWheelchairPreferred] when it changes.
     */
    fun whenWheelchairPreferenceChanges(): Observable<Boolean>
}
