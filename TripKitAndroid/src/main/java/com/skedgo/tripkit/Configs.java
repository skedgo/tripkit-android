package com.skedgo.tripkit;

import android.content.Context;

import androidx.annotation.Nullable;

import com.skedgo.tripkit.configuration.Key;
import com.skedgo.tripkit.routing.ExtraQueryMapProvider;

import io.reactivex.functions.Consumer;

import org.immutables.value.Value;
import org.joda.time.DateTime;

import java.util.concurrent.Callable;

public interface Configs {
    Context context();

    public abstract Callable<Key> key();

    @Nullable
    public abstract Consumer<Throwable> errorHandler();

    @Nullable
    public abstract Callable<Co2Preferences> co2PreferencesFactory();

    @Nullable
    public abstract Callable<TripPreferences> tripPreferencesFactory();

    @Nullable
    public abstract ExtraQueryMapProvider extraQueryMapProvider();

    @Nullable
    public abstract Callable<String> userTokenProvider();

    @Nullable
    public abstract DateTimePickerConfig dateTimePickerConfig();

    @Nullable
    public abstract TransportModeConfig transportModeConfig();

    @Nullable
    public abstract RoutesScreenConfig routeScreenConfig();

    /**
     * @return A factory to create a sort of adapter that specifies a base url
     * to override all the 'satapp' requests made by TripKit's apis.
     * The factory is in use only when {@link #debuggable()} is true.
     */
    @Nullable
    public abstract Callable<String> baseUrlAdapterFactory();

    public boolean debuggable();

    public boolean isUuidOptedOut();

//  @Value.Default public boolean debuggable() { return false; }
//
//  @Value.Default public boolean isUuidOptedOut() { return false; }

    public boolean hideTripMetrics();

    public boolean showReportProblemOnTripAction();

}
