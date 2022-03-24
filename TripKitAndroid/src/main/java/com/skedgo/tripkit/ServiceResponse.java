package com.skedgo.tripkit;

import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.tripkit.alerts.AlertBlock;
import com.skedgo.tripkit.alerts.ModeInfo;
import com.skedgo.tripkit.common.model.RealtimeAlert;
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
  @Nullable String realTimeStatus();
  List<Shape> shapes();
  @Nullable ModeInfo modeInfo();
  @Nullable RealTimeVehicle realtimeVehicle();
  @Nullable List<RealTimeVehicle> realtimeAlternativeVehicle();
  @Nullable List<RealtimeAlert> alerts();
}