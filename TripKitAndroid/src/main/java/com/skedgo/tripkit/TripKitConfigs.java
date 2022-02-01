package com.skedgo.tripkit;

import android.content.Context;
import androidx.annotation.Nullable;

import com.skedgo.tripkit.routing.ExtraQueryMapProvider;

import io.reactivex.functions.Consumer;
import org.immutables.value.Value;

import com.skedgo.tripkit.configuration.Key;

import java.util.concurrent.Callable;

@Value.Immutable
public abstract class TripKitConfigs implements Configs {
  public static ImmutableTripKitConfigs.Builder builder() {
    return ImmutableTripKitConfigs.builder();
  }

  @Value.Default public boolean debuggable() { return false; }

  @Value.Default public boolean isUuidOptedOut() { return false; }

  @Value.Default public boolean hideTripMetrics() { return false; }

}
