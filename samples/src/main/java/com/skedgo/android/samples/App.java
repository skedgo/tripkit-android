package com.skedgo.android.samples;

import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.skedgo.android.tripkit.Configs;
import com.skedgo.android.tripkit.TripKit;

import net.danlew.android.joda.JodaTimeAndroid;

import rx.functions.Action1;

public final class App extends Application {
  public static TripKit tripKit() {
    return TripKit.singleton();
  }

  @Override public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    JodaTimeAndroid.init(this);
    TripKit.initialize(
        Configs.builder()
            .context(this)
            .regionEligibility("xum")
            .debuggable(BuildConfig.DEBUG)
            .errorHandler(new Action1<Throwable>() {
              @Override public void call(Throwable error) {
                Log.e(App.class.getSimpleName(), error.getMessage(), error);
              }
            })
            .build());
  }
}