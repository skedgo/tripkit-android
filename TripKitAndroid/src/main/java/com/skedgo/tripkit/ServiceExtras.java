package com.skedgo.tripkit;

import com.skedgo.tripkit.common.model.ServiceStop;

import java.util.List;

import androidx.annotation.Nullable;

public final class ServiceExtras {
    @Nullable
    public final String platform;
    @Nullable
    public final List<ServiceStop> stops;

    public ServiceExtras(@Nullable String platform,
                         @Nullable List<ServiceStop> stops) {
        this.platform = platform;
        this.stops = stops;
    }
}