package com.skedgo.android.tripkit;

import android.support.annotation.VisibleForTesting;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;
import rx.functions.Action1;
import rx.functions.Actions;

@Singleton
@Component(modules = MainModule.class)
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
        instance = DaggerTripKit.builder()
            .mainModule(new MainModule(configs))
            .build();
      }

      FetchRegionsService.scheduleAsync(configs.context())
          .subscribe(Actions.empty(), instance.getErrorHandler());
    }
  }

  public abstract RegionService getRegionService();
  public abstract RouteService getRouteService();
  public abstract OkHttpClient getOkHttpClient();
  public abstract okhttp3.OkHttpClient getOkHttpClient3();
  public abstract ServiceExtrasService getServiceExtrasService();
  public abstract Reporter getReporter();
  public abstract BookingResolver getBookingResolver();
  public abstract LocationInfoService getLocationInfoService();
  @VisibleForTesting abstract RegionDatabaseHelper getRegionDatabaseHelper();
  @VisibleForTesting abstract HttpLoggingInterceptor getHttpLoggingInterceptor();
  abstract TripUpdater getTripUpdater();
  abstract Action1<Throwable> getErrorHandler();
}