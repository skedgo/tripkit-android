package com.skedgo.tripkit.routing;

import androidx.annotation.Nullable;

public final class TripSegments {
    private TripSegments() {
    }

    @Nullable
    public static ServiceColor getTransportColor(@Nullable TripSegment segment) {
        if (segment == null) {
            return null;
        } else {
            final ServiceColor color = segment.getServiceColor();
            if (color != null) {
                return color;
            } else {
                final ModeInfo modeInfo = segment.modeInfo;
                if (modeInfo != null) {
                    return modeInfo.getColor();
                } else {
                    return null;
                }
            }
        }
    }
}