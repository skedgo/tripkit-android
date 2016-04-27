package com.skedgo.android.tripkit.booking.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersAuthProvider.class)
public interface AuthProvider {
  @Nullable String modeIdentifier();
  @Nullable String provider();
  @Nullable String action();
  @Nullable String url();
  @Nullable String actionTitle();
  @Nullable String status();
}