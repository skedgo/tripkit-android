package com.skedgo.android.tripkit;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersCarPark.class)
public abstract class CarPark {
  public abstract String identifier();
  public abstract String name();
  public abstract int totalSpaces();
  public abstract int availableSpaces();
  public abstract long lastUpdate();
}