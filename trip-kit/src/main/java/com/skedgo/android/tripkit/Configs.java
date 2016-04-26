package com.skedgo.android.tripkit;

import android.content.Context;
import android.support.annotation.Nullable;

import org.immutables.value.Value;

import rx.functions.Action1;
import rx.functions.Func0;

import static org.immutables.value.Value.Style.BuilderVisibility.PACKAGE;
import static org.immutables.value.Value.Style.ImplementationVisibility.PRIVATE;

@Value.Immutable
@Value.Style(visibility = PRIVATE, builderVisibility = PACKAGE)
public abstract class Configs {
  public static Builder builder() {
    return new ConfigsBuilder();
  }

  public abstract Context context();
  public abstract String regionEligibility();
  @Nullable public abstract Action1<Throwable> errorHandler();
  @Nullable public abstract ExcludedTransitModesAdapter excludedTransitModesAdapter();
  @Nullable public abstract Func0<Co2Preferences> co2PreferencesFactory();
  @Nullable public abstract Func0<TripPreferences> tripPreferencesFactory();
  @Nullable public abstract Func0<String> userTokenProvider();

  /**
   * @return A factory to create a sort of adapter that specifies a base url
   * to override all the 'satapp' requests made by TripKit's apis.
   * The factory is in use only when {@link #debuggable()} is true.
   */
  @Nullable public abstract Func0<Func0<String>> baseUrlAdapterFactory();

  @Value.Default public boolean debuggable() {
    return false;
  }

  public interface Builder {
    Builder context(Context context);
    Builder regionEligibility(String regionEligibility);
    Builder debuggable(boolean debuggable);
    Builder errorHandler(Action1<Throwable> errorHandler);
    Builder excludedTransitModesAdapter(ExcludedTransitModesAdapter excludedTransitModesAdapter);
    Builder co2PreferencesFactory(Func0<Co2Preferences> co2PreferencesFactory);
    Builder tripPreferencesFactory(Func0<TripPreferences> tripPreferencesFactory);
    Builder baseUrlAdapterFactory(Func0<Func0<String>> baseUrlAdapterFactory);
    Builder userTokenProvider(Func0<String> userTokenProvider);
    Configs build();
  }
}