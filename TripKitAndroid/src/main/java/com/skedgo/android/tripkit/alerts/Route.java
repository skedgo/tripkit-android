package com.skedgo.android.tripkit.alerts;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersRoute.class)
public abstract class Route {
  public abstract String id();

   public abstract String name();

  @Value.Default int type() {
    return -1;
  }
}
