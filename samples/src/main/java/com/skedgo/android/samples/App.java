package com.skedgo.android.samples;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.skedgo.android.tripkit.TripKit;

public final class App extends Application {
  @Override public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    TripKit.with(this)
        .getOkHttpClient()
        .networkInterceptors().add(new StethoInterceptor());
  }
}