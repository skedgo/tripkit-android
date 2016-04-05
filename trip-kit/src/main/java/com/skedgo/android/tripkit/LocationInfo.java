package com.skedgo.android.tripkit;

import android.support.annotation.Nullable;

import com.skedgo.android.common.model.ScheduledStop;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public interface LocationInfo {
  @Nullable LocationInfoDetails details();
  @Nullable ScheduledStop stop();
}