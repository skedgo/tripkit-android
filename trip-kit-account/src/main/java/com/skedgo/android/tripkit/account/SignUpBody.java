package com.skedgo.android.tripkit.account;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersSignUpBody.class)
public abstract class SignUpBody {
  public static ImmutableSignUpBody.Builder builder() {
    return ImmutableSignUpBody.builder();
  }

  @Nullable public abstract String name();
  public abstract String password();
  public abstract String username();
}
