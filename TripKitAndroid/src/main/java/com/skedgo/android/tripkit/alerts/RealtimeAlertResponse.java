package com.skedgo.android.tripkit.alerts;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersRealtimeAlertResponse.class)
public interface RealtimeAlertResponse {
  @Nullable List<AlertBlock> alerts();
}