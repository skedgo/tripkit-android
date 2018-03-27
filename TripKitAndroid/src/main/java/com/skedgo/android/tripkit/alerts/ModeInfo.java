package com.skedgo.android.tripkit.alerts;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import skedgo.tripkit.routing.ServiceColor;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersModeInfo.class)
public interface ModeInfo {
  @Nullable ServiceColor color();
  @Nullable String identifier();
  String modeIdentifier();
}