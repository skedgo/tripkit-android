package skedgo.tripkit.android;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skedgo.android.tripkit.Configs;
import com.skedgo.android.tripkit.HttpClientModule;
import com.skedgo.android.tripkit.LocationInfoService;
import com.skedgo.android.tripkit.MainModule;
import com.skedgo.android.tripkit.RegionService;
import com.skedgo.android.tripkit.TripUpdater;
import com.skedgo.android.tripkit.bookingproviders.BookingResolver;
import skedgo.tripkit.a2brouting.A2bRoutingDataModule;
import com.skedgo.android.tripkit.tsp.TspModule;

import javax.inject.Singleton;

import dagger.Component;
import rx.functions.Action1;
import rx.functions.Actions;
import skedgo.tripkit.a2brouting.RouteService;

@Singleton
@Component(modules = {
    HttpClientModule.class,
    A2bRoutingDataModule.class,
    TspModule.class,
    MainModule.class
})
public abstract class TripKit {
  private static TripKit instance;

  public static TripKit getInstance() {
    synchronized (TripKit.class) {
      if (instance == null) {
        throw new IllegalStateException("Must initialize TripKit before using getInstance()");
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
   * Otherwise, just go with {@link #initialize(Configs)} instead.
   *
   * @param context A {@link Context} to launch {@link FetchRegionsService}.
   * @param tripKit Can be created via {@link DaggerTripKit}.
   */
  public static void initialize(@NonNull Context context, @NonNull TripKit tripKit) {
    synchronized (TripKit.class) {
      if (instance == null) {
        instance = tripKit;
      }
      FetchRegionsService.Companion.scheduleAsync(context)
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

      FetchRegionsService.Companion.scheduleAsync(configs.context())
          .subscribe(Actions.empty(), instance.getErrorHandler());
    }
  }

  public abstract Configs configs();
  public abstract RegionService getRegionService();
  public abstract RouteService getRouteService();
  public abstract okhttp3.OkHttpClient getOkHttpClient3();
  public abstract BookingResolver getBookingResolver();
  public abstract LocationInfoService getLocationInfoService();
  public abstract TripUpdater getTripUpdater();

  public abstract A2bRoutingComponent a2bRoutingComponent();
  public abstract AnalyticsComponent analyticsComponent();
  public abstract DateTimeComponent dateTimeComponent();

  public abstract Action1<Throwable> getErrorHandler();
}
