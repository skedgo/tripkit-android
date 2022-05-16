package com.skedgo.tripkit;

import org.immutables.value.Value;

@Value.Immutable
public abstract class TripKitConfigs implements Configs {
    public static ImmutableTripKitConfigs.Builder builder() {
        return ImmutableTripKitConfigs.builder();
    }

    @Value.Default
    public boolean debuggable() {
        return false;
    }

    @Value.Default
    public boolean isUuidOptedOut() {
        return false;
    }

    @Value.Default
    public boolean hideTripMetrics() {
        return false;
    }

    @Value.Default
    public boolean showReportProblemOnTripAction() {
        return false;
    }

}
