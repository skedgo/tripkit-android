package com.skedgo.android.tripkit.waypoints;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersConfigurationParams.class)
public abstract class ConfigurationParams {

  public abstract int ws();
  public abstract int cs();
  public abstract int tt();
  public abstract String unit();
  public abstract int v();
  public abstract String wp();
  public abstract boolean ir();
  public abstract boolean wheelchair();

}
