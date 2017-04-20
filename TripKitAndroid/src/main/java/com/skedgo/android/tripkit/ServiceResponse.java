package com.skedgo.android.tripkit;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;
import skedgo.tripkit.routing.RealTimeVehicle;
import skedgo.tripkit.routing.Shape;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersServiceResponse.class)
public interface ServiceResponse {
  String realTimeStatus();
  String type();
  List<Shape> shapes();
  @Nullable RealTimeVehicle realtimeVehicle();
  @Nullable List<RealTimeVehicle> realtimeAlternativeVehicle();
}