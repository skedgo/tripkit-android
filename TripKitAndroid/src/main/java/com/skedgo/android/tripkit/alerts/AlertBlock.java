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

  @Nullable String disruptionType();
  @Nullable Long fromDate();
  @Nullable String modeIdentifier();
  @Nullable String[] operators();
  @Nullable Long publishedOn();
  @Nullable Route[] routes();
  @Nullable Long toDate();
  @Nullable String transportType();
  @Nullable String[] stopCodes();
  @Nullable String[] serviceTripIDs();
}

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersRoute.class)
interface Route {
  String id();

  String name();

  int type();
}