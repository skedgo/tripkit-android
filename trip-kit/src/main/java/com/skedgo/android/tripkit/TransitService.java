package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.RealTimeVehicle;
import com.skedgo.android.common.model.ServiceShape;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Gson.TypeAdapters
public interface TransitService {

  List<ServiceShape> shapes();
  String realTimeStatus();
  RealTimeVehicle realtimeVehicle();
  List<RealTimeVehicle> realtimeAlternativeVehicle();

}