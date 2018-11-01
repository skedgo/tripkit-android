package com.skedgo.android.tripkit.booking;

import androidx.annotation.Nullable;

import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
public abstract class ExternalOAuth {

  public abstract String authServiceId();
  public abstract String token();
  @Nullable public abstract String refreshToken();
  public abstract long expiresIn();

  @Value.Default public String id() {
    return UUID.randomUUID().toString();
  }

}
