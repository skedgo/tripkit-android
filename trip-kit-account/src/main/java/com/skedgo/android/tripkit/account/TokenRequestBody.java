package com.skedgo.android.tripkit.account;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersTokenRequestBody.class)
public abstract class TokenRequestBody {
  public abstract String code();
  public abstract String grant_type();
}


