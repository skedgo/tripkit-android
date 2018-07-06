package com.skedgo.android.tripkit;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.android.common.model.ScheduledStop;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersLocationInfo.class)
public interface LocationInfo {
  @Nullable LocationInfoDetails details();
  @Nullable ScheduledStop stop();
  @Nullable CarPark carPark();
  double lat();
  double lng();
}