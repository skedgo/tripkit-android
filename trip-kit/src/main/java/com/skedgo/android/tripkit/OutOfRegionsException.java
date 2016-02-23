package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skedgo.android.common.model.Location;

public final class OutOfRegionsException extends RuntimeException {
  private final Location location;

  public OutOfRegionsException(@NonNull Location location, @Nullable String detailMessage) {
    super(detailMessage);
    this.location = location;
  }

  @NonNull
  public Location getLocation() {
    return location;
  }
}