package com.skedgo.android.tripkit.booking;

import org.immutables.value.Value;

import java.util.UUID;

import static org.immutables.value.Value.Style.BuilderVisibility.PACKAGE;
import static org.immutables.value.Value.Style.ImplementationVisibility.PRIVATE;

@Value.Immutable
@Value.Style(visibility = PRIVATE, builderVisibility = PACKAGE)
public abstract class ExternalOAuth {

  public static Builder builder() {
    return new ExternalOAuthBuilder();
  }

  public abstract String authServiceId();
  public abstract String token();
  public abstract int expiresIn();

  @Value.Default public String id() {
    return UUID.randomUUID().toString();
  }

  public interface Builder {
    Builder authServiceId(String authServiceId);
    Builder token(String token);
    Builder expiresIn(int expiresIn);
    ExternalOAuth build();
  }

}
