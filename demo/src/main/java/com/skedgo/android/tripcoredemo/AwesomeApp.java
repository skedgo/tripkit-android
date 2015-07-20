package com.skedgo.android.tripcoredemo;

import android.app.Application;
import android.content.Context;

import com.skedgo.android.tripcore.RouteComponent;

public class AwesomeApp extends Application {
  private RouteComponent routeComponent; // Lazily initialized.

  public synchronized static RouteComponent getRouteComponent(Context context) {
    final AwesomeApp app = (AwesomeApp) context.getApplicationContext();
    if (app.routeComponent == null) {
      app.routeComponent = RouteComponent.create(app);
    }

    return app.routeComponent;
  }
}