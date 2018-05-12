package com.skedgo.android.tripkit;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;
import skedgo.tripkit.routing.RealTimeVehicle;
import skedgo.tripkit.routing.Shape;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@Immutable
@TypeAdapters
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersServiceResponse.class)
public interface ServiceResponse {
  String realTimeStatus();
  String type();
  List<Shape> shapes();
  @Nullable RealTimeVehicle realtimeVehicle();
  @Nullable List<RealTimeVehicle> realtimeAlternativeVehicle();
}