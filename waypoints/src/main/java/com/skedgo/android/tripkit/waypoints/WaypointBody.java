package com.skedgo.android.tripkit.waypoints;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersWaypointBody.class)
public abstract class WaypointBody {

  public abstract ConfigurationParams config();
  public abstract List<WaypointSegmentAdapter> segments();

}