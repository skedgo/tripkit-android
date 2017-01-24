package com.skedgo.android.tripkit;

import android.content.Context;
import android.support.annotation.Nullable;

import com.skedgo.android.tripkit.routing.ExtraQueryMapProvider;

import org.immutables.value.Value;

import retrofit2.Retrofit;
import rx.functions.Action1;
import rx.functions.Func0;

@Value.Immutable
public abstract class Configs {
  public static Retrofit.Builder builder() { return new Retrofit.Builder(); }

  public abstract Context context();
  public abstract String regionEligibility();
  @Nullable public abstract Action1<Throwable> errorHandler();
  @Nullable public abstract ExcludedTransitModesAdapter excludedTransitModesAdapter();
  @Nullable public abstract Func0<Co2Preferences> co2PreferencesFactory();
  @Nullable public abstract Func0<TripPreferences> tripPreferencesFactory();
  @Nullable public abstract ExtraQueryMapProvider extraQueryMapProvider();

  /**
   * @return A factory to retrieve user token obtained via TripGo Account API.
   * As tripkit is needed to be intialized to inject it via tripkit account, it cannot be part of the builder
   * @see <a href="http://planck.buzzhives.com/tripgodata/account/resource_AccountSpecificRestService.html">TripGo Account API</a>
   */
  private Func0<String> userTokenProvider;

  @Nullable public Func0<String> userTokenProvider() {
    return userTokenProvider;
  }

  public void setUserTokenProvider(Func0<String> userTokenProvider) {
    this.userTokenProvider = userTokenProvider;
  }

  /**
   * @return A factory to create a sort of adapter that specifies a base url
   * to override all the 'satapp' requests made by TripKit's apis.
   * The factory is in use only when {@link #debuggable()} is true.
   */
  @Nullable public abstract Func0<Func0<String>> baseUrlAdapterFactory();

  @Value.Default public boolean debuggable() { return false; }

  @Value.Default public boolean isUuidOptedOut() { return false; }
}