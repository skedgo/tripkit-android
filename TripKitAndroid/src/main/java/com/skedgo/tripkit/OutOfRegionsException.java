package com.skedgo.tripkit;

import androidx.annotation.Nullable;

public final class OutOfRegionsException extends RuntimeException {
    private final double latitude;
    private final double longitude;

    public OutOfRegionsException(
        @Nullable String detailMessage,
        double latitude,
        double longitude) {
        super(detailMessage);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double latitude() {
        return latitude;
    }

    public double longitude() {
        return longitude;
    }
}
