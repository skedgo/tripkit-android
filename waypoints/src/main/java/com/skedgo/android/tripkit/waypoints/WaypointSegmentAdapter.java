package com.skedgo.android.tripkit.waypoints;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersWaypointSegmentAdapter.class)
public abstract class WaypointSegmentAdapter {

  public abstract String start();
  public abstract String end();
  public abstract List<String> modes();

  @Value.Default public int startTime() {return 0;}

  @Value.Default public int endTime() {return 0;}

  @Nullable public abstract String serviceTripId();
  @Nullable public abstract String operator();
  @Nullable public abstract String region();

}
