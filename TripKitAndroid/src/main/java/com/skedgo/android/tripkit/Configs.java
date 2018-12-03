package com.skedgo.android.tripkit;

import android.content.Context;
import androidx.annotation.Nullable;

import com.skedgo.android.tripkit.routing.ExtraQueryMapProvider;

import org.immutables.value.Value;

import rx.functions.Action1;
import rx.functions.Func0;
import skedgo.tripkit.configuration.Key;

@Value.Immutable
public abstract class Configs {
  public static ImmutableConfigs.Builder builder() {
    return ImmutableConfigs.builder();
  }

  public abstract Context context();
  public abstract Func0<Key> key();
  @Nullable public abstract Action1<Throwable> errorHandler();
  @Nullable public abstract Func0<Co2Preferences> co2PreferencesFactory();
  @Nullable public abstract Func0<TripPreferences> tripPreferencesFactory();
  @Nullable public abstract ExtraQueryMapProvider extraQueryMapProvider();
  @Nullable public abstract Func0<String> userTokenProvider();

  /**
   * @return A factory to create a sort of adapter that specifies a base url
   * to override all the 'satapp' requests made by TripKit's apis.
   * The factory is in use only when {@link #debuggable()} is true.
   */
  @Nullable public abstract Func0<Func0<String>> baseUrlAdapterFactory();

  @Value.Default public boolean debuggable() { return false; }

  @Value.Default public boolean isUuidOptedOut() { return false; }
}
