package com.skedgo.android.tripkit.account.model;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class LogInBody {
  public abstract String password();
  public abstract String username();
}