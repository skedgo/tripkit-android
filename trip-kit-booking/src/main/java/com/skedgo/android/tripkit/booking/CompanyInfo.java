package com.skedgo.android.tripkit.booking;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersCompanyInfo.class)
public interface CompanyInfo {
  @Nullable String website();
}