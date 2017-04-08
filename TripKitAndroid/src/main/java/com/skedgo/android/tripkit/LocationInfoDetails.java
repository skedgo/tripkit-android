package com.skedgo.android.tripkit;

import android.support.annotation.Nullable;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class LocationInfoDetails {
  @Nullable public abstract String w3w();
  @Nullable public abstract String w3wInfoURL();
}