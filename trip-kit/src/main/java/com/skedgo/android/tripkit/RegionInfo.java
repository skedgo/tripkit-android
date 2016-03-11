package com.skedgo.android.tripkit;

import android.support.annotation.Nullable;

import com.skedgo.android.common.model.ModeInfo;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Gson.TypeAdapters
@Value.Immutable
interface RegionInfo {
  @Nullable List<ModeInfo> transitModes();
  @Nullable Paratransit paratransit();
}