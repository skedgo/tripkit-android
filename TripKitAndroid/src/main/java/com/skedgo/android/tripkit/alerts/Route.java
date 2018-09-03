package com.skedgo.android.tripkit.alerts;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import skedgo.tripkit.routing.ModeInfo;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@Immutable
@TypeAdapters
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersRoute.class)
public abstract class Route {
  public abstract String id();

  @Nullable public abstract String name();

  @Nullable public abstract String number();

  @Nullable public abstract ModeInfo modeInfo();

  @Value.Default int type() {
    return -1;
  }
}
