package com.skedgo.tripkit.booking;

import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersCompanyInfo.class)
public interface CompanyInfo {
  @Nullable String name();
  @Nullable String website();
}