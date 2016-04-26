package com.skedgo.android.accountkit.model;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class LogOutResponse {
  @Value.Default public boolean changed() {
    return false;
  }
}