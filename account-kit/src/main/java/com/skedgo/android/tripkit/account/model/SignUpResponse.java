package com.skedgo.android.tripkit.account.model;

import android.support.annotation.Nullable;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class SignUpResponse {
  @Nullable public abstract String userToken();

  @Value.Default public boolean changed() {
    return false;
  }
}