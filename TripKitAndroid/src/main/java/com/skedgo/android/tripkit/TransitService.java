package com.skedgo.android.tripkit;

import skedgo.tripkit.routing.RealTimeVehicle;
import com.skedgo.android.common.model.Shape;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Gson.TypeAdapters
public interface TransitService {

  List<Shape> shapes();
  String realTimeStatus();
  RealTimeVehicle realtimeVehicle();
  List<RealTimeVehicle> realtimeAlternativeVehicle();

}