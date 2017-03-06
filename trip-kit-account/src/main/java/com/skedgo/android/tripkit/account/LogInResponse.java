package com.skedgo.android.tripkit.account;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersLogInResponse.class)
public abstract class LogInResponse {
  @Nullable public abstract String userToken();
}
