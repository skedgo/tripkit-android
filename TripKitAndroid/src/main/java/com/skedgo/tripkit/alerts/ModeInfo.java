package com.skedgo.tripkit.alerts;

import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import skedgo.tripkit.routing.ServiceColor;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@Immutable
@TypeAdapters
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersModeInfo.class)
public interface ModeInfo {
  @Nullable ServiceColor color();
  @Nullable String identifier();
  @Nullable String alt();
}