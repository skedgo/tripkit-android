package com.skedgo.tripkit;

import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.tripkit.routing.RealTimeVehicle;
import com.skedgo.tripkit.routing.Shape;

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