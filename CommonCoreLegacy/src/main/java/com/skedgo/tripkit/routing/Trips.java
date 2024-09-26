package com.skedgo.tripkit.routing;

import com.skedgo.tripkit.common.model.location.Location;

import androidx.annotation.Nullable;

/**
 * Use extension functions in {@link TripExtensionsKt} instead.
 */
@Deprecated
public final class Trips {
    private Trips() {
    }

    /**
     * Get departure time of a trip null-safely.
     * <p>
     * Use {@link TripExtensionsKt#startDateTime(Trip)} instead.
     */
    @Deprecated
    @Nullable
    public static String getDepartureTimezone(@Nullable Trip trip) {
        if (trip == null) {
            return null;
        }

        final Location departure = trip.getFrom();
        return departure == null ? null : departure.getTimeZone();
    }
}