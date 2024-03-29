package com.skedgo.tripkit.booking;

import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersAuthProvider.class)
public interface AuthProvider {
  @Nullable String modeIdentifier();
  @Nullable String provider();
  @Nullable String action();
  @Nullable String url();
  @Nullable String actionTitle();
  @Nullable String status();
  @Nullable CompanyInfo companyInfo();
}