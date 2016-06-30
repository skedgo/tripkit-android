package com.skedgo.android.tripkit;

import com.google.gson.annotations.SerializedName;
import com.skedgo.android.common.model.RealTimeVehicle;
import com.skedgo.android.common.model.RealtimeAlert;
import com.skedgo.android.common.model.ServiceShape;
import com.skedgo.android.common.model.Shape;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Gson.TypeAdapters
public interface TransitService {

  @SerializedName("shapes") List<ServiceShape> shapes();
  @SerializedName("realTimeStatus") String realTimeStatus();
  @SerializedName("realtimeVehicle") RealTimeVehicle realtimeVehicle();
  @SerializedName("realtimeAlternativeVehicle") List<RealTimeVehicle> realtimeAlternativeVehicle();



}