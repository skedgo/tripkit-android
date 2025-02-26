package com.skedgo.tripkit

import org.immutables.value.Value
import org.immutables.value.Value.Default
import org.immutables.value.Value.Immutable

@Immutable
abstract class TripKitConfigs : Configs {
    companion object {
        fun builder(): ImmutableTripKitConfigs.Builder {
            return ImmutableTripKitConfigs.builder()
        }
    }

    @Default
    override fun debuggable(): Boolean {
        return false
    }

    @Default
    override fun isUuidOptedOut(): Boolean {
        return false
    }

    @Default
    override fun hideTripMetrics(): Boolean {
        return false
    }

    @Default
    override fun showReportProblemOnTripAction(): Boolean {
        return false
    }

    @Default
    override fun showOperatorNames(): Boolean {
        return false
    }
}
