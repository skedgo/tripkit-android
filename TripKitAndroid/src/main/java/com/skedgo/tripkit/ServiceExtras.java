package com.skedgo.tripkit;

import androidx.annotation.Nullable;

import com.skedgo.android.common.model.ServiceStop;

import java.util.List;

public final class ServiceExtras {
  @Nullable public final String platform;
  @Nullable public final List<ServiceStop> stops;

  public ServiceExtras(@Nullable String platform,
                       @Nullable List<ServiceStop> stops) {
    this.platform = platform;
    this.stops = stops;
  }
}