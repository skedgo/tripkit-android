package com.skedgo.tripkit.common.model;

import android.os.Build;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class Regions {
    private Regions() {
    }

    public static boolean equals(@Nullable Region a, @Nullable Region b) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.equals(a, b);
        } else {
            return a != null && b != null && a.equals(b);
        }
    }

    @NonNull
    public static Region createInterRegion(
        @NonNull Region departureRegion,
        @NonNull Region arrivalRegion) {
        return new InterRegion(departureRegion, arrivalRegion);
    }

    static final class InterRegion extends Region {
        InterRegion(@NonNull Region departureRegion, @NonNull Region arrivalRegion) {
            setName(departureRegion.getName() + "_" + arrivalRegion.getName());

            final ArrayList<String> departureModeIds = departureRegion.getTransportModeIds();
            final ArrayList<String> arrivalModeIds = arrivalRegion.getTransportModeIds();
            if (departureModeIds != null && arrivalModeIds != null) {
                final Set<String> unionModeIds = new LinkedHashSet<>(
                    departureModeIds.size() + arrivalModeIds.size() + 1
                );
                unionModeIds.add(TransportMode.ID_AIR);
                unionModeIds.addAll(departureModeIds);
                unionModeIds.addAll(arrivalModeIds);
                setTransportModeIds(new ArrayList<>(unionModeIds));
            } else if (departureModeIds != null) {
                final Set<String> unionModeIds = new LinkedHashSet<>(
                    departureModeIds.size() + 1
                );
                unionModeIds.add(TransportMode.ID_AIR);
                unionModeIds.addAll(departureModeIds);
                setTransportModeIds(new ArrayList<>(unionModeIds));
            } else if (arrivalModeIds != null) {
                final Set<String> unionModeIds = new LinkedHashSet<>(
                    arrivalModeIds.size() + 1
                );
                unionModeIds.add(TransportMode.ID_AIR);
                unionModeIds.addAll(arrivalModeIds);
                setTransportModeIds(new ArrayList<>(unionModeIds));
            }

            final ArrayList<String> departureUrls = departureRegion.getURLs();
            if (departureUrls != null) {
                setURLs(new ArrayList<>(departureUrls));
            }

            setTimezone(departureRegion.getTimezone());
            setEncodedPolyline(departureRegion.getEncodedPolyline());
        }
    }
}