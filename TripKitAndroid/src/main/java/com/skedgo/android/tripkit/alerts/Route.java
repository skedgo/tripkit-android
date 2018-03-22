package com.skedgo.android.tripkit.alerts;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import skedgo.tripkit.routing.ModeInfo;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersRoute.class)
public abstract class Route {
  public abstract String id();

  public abstract String name();

  @Nullable public abstract String number();

  public abstract ModeInfo modeInfo();

  @Value.Default int type() {
    return -1;
  }
}
