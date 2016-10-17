package com.skedgo.android.tripkit.booking;

import android.support.annotation.Nullable;

import org.immutables.value.Value;

import java.util.UUID;

import static org.immutables.value.Value.Style.BuilderVisibility.PACKAGE;
import static org.immutables.value.Value.Style.ImplementationVisibility.PRIVATE;

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
