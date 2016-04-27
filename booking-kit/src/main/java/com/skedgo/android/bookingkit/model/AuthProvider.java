package com.skedgo.android.bookingkit.model;

import android.support.annotation.Nullable;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public interface AuthProvider {
  @Nullable String modeIdentifier();
  @Nullable String provider();
  @Nullable String action();
  @Nullable String url();
  @Nullable String actionTitle();
  @Nullable String status();
}