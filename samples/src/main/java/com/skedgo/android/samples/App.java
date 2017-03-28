package com.skedgo.android.samples;

import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.skedgo.android.tripkit.Configs;
import com.skedgo.android.tripkit.TripKit;

import net.danlew.android.joda.JodaTimeAndroid;

import rx.functions.Action1;
import rx.functions.Func0;
import skedgo.tripkit.configuration.ApiKey;

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
            .apiKey(new Func0<ApiKey>() {
              @Override public ApiKey call() {
                return new ApiKey("dfca814e1a213c3e75ac8d4ecffecdb6");
              }
            })
            .debuggable(BuildConfig.DEBUG)
            .errorHandler(new Action1<Throwable>() {
              @Override public void call(Throwable error) {
                Log.e(App.class.getSimpleName(), error.getMessage(), error);
              }
            })
            .build());
  }
}