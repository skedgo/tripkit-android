package com.skedgo.android.tripkit;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import static org.immutables.value.Value.Style.BuilderVisibility.PACKAGE;

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(
    visibility = Value.Style.ImplementationVisibility.PACKAGE,
    builderVisibility = PACKAGE
)
public abstract class LocationInfoDetails {

  public abstract String w3w();
  public abstract String w3wInfoURL();
}
