package com.skedgo.android.tripkit;

import com.squareup.okhttp.OkHttpClient;

import rx.functions.Action1;
import rx.functions.Actions;

public abstract class TripKit {
  private static TripKit instance;

  public static TripKit singleton() {
    synchronized (TripKit.class) {
      if (instance == null) {
        throw new IllegalStateException("Must initialize TripKit before using singleton()");
      }

      return instance;
    }
  }

  public static void initialize(Configs configs) {
    synchronized (TripKit.class) {
      if (configs == null) {
        throw new IllegalStateException("Must initialize Configs before using initialize()");
      }

      if (instance == null) {
        instance = new TripKitImpl(configs);
      }

      FetchRegionsService.scheduleAsync(configs.context())
          .subscribe(Actions.empty(), instance.getErrorHandler());
    }
  }

  public abstract RegionService getRegionService();
  public abstract RouteService getRouteService();
  public abstract OkHttpClient getOkHttpClient();
  public abstract ServiceExtrasService getServiceExtrasService();
  public abstract Reporter getReporter();
  public abstract InterAppCommunicator getInterAppCommunicator();
  abstract TripUpdater getTripUpdater();
  abstract Action1<Throwable> getErrorHandler();
}