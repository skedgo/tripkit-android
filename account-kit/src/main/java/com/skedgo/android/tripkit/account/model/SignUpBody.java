package com.skedgo.android.tripkit.account.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersSignUpBody.class)
public abstract class SignUpBody {
  @Nullable public abstract String name();
  public abstract String password();
  public abstract String username();
}