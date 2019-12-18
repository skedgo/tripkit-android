package com.skedgo.tripkit.alerts;

import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.tripkit.common.model.RealtimeAlert;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@Immutable
@TypeAdapters
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersAlertBlock.class)
public interface AlertBlock {
  @Nullable RealtimeAlert alert();

  @Nullable String disruptionType();
  @Nullable String[] operators();
  @Nullable Route[] routes();
  @Nullable ModeInfo modeInfo();
  @Nullable String[] stopCodes();
  @Nullable String[] serviceTripIDs();
}