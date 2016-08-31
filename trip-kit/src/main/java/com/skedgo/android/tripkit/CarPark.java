package com.skedgo.android.tripkit;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class CarPark {
  public abstract String identifier();
  public abstract String name();
  public abstract int totalSpaces();
  public abstract int availableSpaces();
  public abstract long lastUpdate();
}
