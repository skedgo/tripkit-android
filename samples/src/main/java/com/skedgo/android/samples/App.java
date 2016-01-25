package com.skedgo.android.samples;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.skedgo.android.tripkit.TripKit;

public final class App extends Application {
  protected static TripKit tripKit;

  public static TripKit tripKit() {
    return tripKit;
  }

  @Override public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);

    tripKit = TripKit.with(this);
    TripKit.setLoggingEnabled(BuildConfig.DEBUG);
    tripKit.getOkHttpClient()
        .networkInterceptors().add(new StethoInterceptor());
  }
}