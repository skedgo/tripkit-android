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
  RealtimeAlert alert();

  String disruptionType();
  String[] operators();
  @Nullable Route[] routes();
  @Nullable String transportType();
  @Nullable String[] stopCodes();
  @Nullable String[] serviceTripIDs();
}

