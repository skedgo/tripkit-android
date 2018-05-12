package com.skedgo.android.tripkit;

import com.google.gson.annotations.JsonAdapter;

import skedgo.tripkit.routing.RealTimeVehicle;
import skedgo.tripkit.routing.Shape;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersTransitService.class)
@Value.Style(passAnnotations = JsonAdapter.class)
public interface TransitService {

  List<Shape> shapes();
  String realTimeStatus();
  RealTimeVehicle realtimeVehicle();
  List<RealTimeVehicle> realtimeAlternativeVehicle();

}