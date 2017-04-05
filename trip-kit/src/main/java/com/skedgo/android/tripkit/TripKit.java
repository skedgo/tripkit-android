package com.skedgo.android.tripkit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.skedgo.android.tripkit.bookingproviders.BookingResolver;
import com.skedgo.android.tripkit.routing.RoutingModule;
import com.skedgo.android.tripkit.tsp.TspModule;

import javax.inject.Singleton;

import dagger.Component;
import rx.functions.Action1;
import rx.functions.Actions;
import skedgo.tripkit.routing.a2b.A2bRoutingDomainModule;
import skedgo.tripkit.routing.a2b.GetA2bTrips;

@Singleton
@Component(modules = {
    HttpClientModule.class,
    RoutingModule.class,
    TspModule.class,
    MainModule.class,
    A2bRoutingDomainModule.class
})
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

  /**
   * This gives a chance to provide a custom {@link TripKit}.
   * One idea is that we can create {@link DaggerTripKit}
   * w/ some customized modules.
   * <p>
   * Note that you should only use this
   * when you totally understand what you're doing.
   * Otherwise, just go w/ {@link #initialize(Configs)}.
   *
   * @param context A {@link Context} to launch {@link FetchRegionsService}.
   * @param tripKit Can be created via {@link DaggerTripKit}.
   */
  public static void initialize(@NonNull Context context, @NonNull TripKit tripKit) {
    synchronized (TripKit.class) {
      if (instance == null) {
        instance = tripKit;
      }
      FetchRegionsService.scheduleAsync(context)
          .subscribe(Actions.empty(), instance.getErrorHandler());
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

  public abstract Configs configs();
  public abstract RegionService getRegionService();
  public abstract RouteService getRouteService();
  public abstract okhttp3.OkHttpClient getOkHttpClient3();
  public abstract Reporter getReporter();
  public abstract BookingResolver getBookingResolver();
  public abstract LocationInfoService getLocationInfoService();
  public abstract TripUpdater getTripUpdater();
  public abstract ReportingApi getReportingApi();
  public abstract GetA2bTrips getGetA2bTrips();
  @VisibleForTesting abstract RegionDatabaseHelper getRegionDatabaseHelper();
  abstract Action1<Throwable> getErrorHandler();
}