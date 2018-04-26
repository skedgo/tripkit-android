package com.skedgo.android.tripkit.alerts;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.android.common.model.RealtimeAlert;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Gson.TypeAdapters
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