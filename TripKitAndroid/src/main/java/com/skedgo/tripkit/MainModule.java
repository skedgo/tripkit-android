package com.skedgo.tripkit;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.android.common.model.GsonAdaptersBooking;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.TransportMode;
import com.skedgo.android.common.util.Gsons;
import com.skedgo.android.common.util.LowercaseEnumTypeAdapterFactory;
import com.skedgo.tripkit.bookingproviders.BookingResolver;
import com.skedgo.tripkit.bookingproviders.BookingResolverImpl;
import com.skedgo.tripkit.data.regions.RegionService;
import com.skedgo.tripkit.data.tsp.GsonAdaptersRegionInfo;
import com.skedgo.tripkit.tsp.GsonAdaptersRegionInfoBody;
import com.skedgo.tripkit.tsp.GsonAdaptersRegionInfoResponse;
import com.skedgo.tripkit.tsp.RegionInfoRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import io.reactivex.schedulers.Schedulers;
import com.skedgo.tripkit.a2brouting.FailoverA2bRoutingApi;
import com.skedgo.tripkit.a2brouting.RouteService;
import com.skedgo.TripKit;
import skedgo.tripkit.configuration.AppVersionNameRepository;
import skedgo.tripkit.configuration.Server;

@Module
public class MainModule {
  private final Configs configs;
  private final Context context;

  public MainModule(@NonNull Configs configs) {
    this.configs = configs;
    context = configs.context().getApplicationContext();
  }

  /**
   * @return A {@link SharedPreferences} that contains
   * internal persistent configs (e.g. UUID) for TripKit.
   */
  @Provides @Named("TripKitPrefs") SharedPreferences preferences() {
    return context.getSharedPreferences("TripKit", Context.MODE_PRIVATE);
  }

  @Provides Configs configs() {
    return configs;
  }

  @Provides RegionsApi getRegionsApi(OkHttpClient httpClient) {
    return new Retrofit.Builder()
        .baseUrl(Server.ApiTripGo.getValue())
        .addConverterFactory(GsonConverterFactory.create(Gsons.createForRegion()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .client(httpClient)
        .build()
        .create(RegionsApi.class);
  }

  @Singleton @Provides RegionDatabaseHelper getRegionDatabaseHelper() {
    return new RegionDatabaseHelper(
        context,
        "regions.db"
    );
  }

  @Singleton @Provides
  RegionService getRegionService(
      RegionDatabaseHelper databaseHelper,
      RegionsApi regionsApi,
      RegionInfoRepository regionInfoRepository) {
    final RegionsFetcher regionsFetcher = new RegionsFetcherImpl(
        regionsApi,
        databaseHelper
    );
    final Cache<List<Region>> regionCache = new CacheImpl<>(
        regionsFetcher.fetchAsync(),
        databaseHelper.loadRegionsAsync()
    );
    final Cache<Map<String, TransportMode>> modeCache = new CacheImpl<>(
        regionsFetcher.fetchAsync(),
        databaseHelper.loadModesAsync()
    );
    return new RegionServiceImpl(
        regionCache,
        modeCache,
        regionsFetcher,
        regionInfoRepository,
        new RegionFinder()
    );
  }

  @Singleton @Provides RouteService routeService(
      FailoverA2bRoutingApi routingApi,
      RegionService regionService,
      Configs configs,
      RegionInfoRepository regionInfoRepository
  ) {
    Co2Preferences co2Preferences = null;
    final Callable<Co2Preferences> co2PreferencesFactory = configs.co2PreferencesFactory();
    if (co2PreferencesFactory != null) {
      try {
        co2Preferences = co2PreferencesFactory.call();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    TripPreferences tripPreferences = null;
    final Callable<TripPreferences> tripPreferencesFactory = configs.tripPreferencesFactory();
    if (tripPreferencesFactory != null) {
      try {
        tripPreferences = tripPreferencesFactory.call();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    final QueryGeneratorImpl queryGenerator = new QueryGeneratorImpl(regionService);
    return new RouteServiceImpl(
            context,
        queryGenerator,
        co2Preferences,
        tripPreferences,
        configs.extraQueryMapProvider(),
        routingApi,
        regionInfoRepository
    );
  }

  @Provides Context context() {
    return configs.context();
  }

  @Singleton @Provides OkHttpClient httpClient(
      OkHttpClient.Builder httpClientBuilder,
      AddCustomHeaders addCustomHeaders) {

    final OkHttpClient.Builder builder = httpClientBuilder
        .addInterceptor(addCustomHeaders);
    if (configs.debuggable()) {
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.level(HttpLoggingInterceptor.Level.BODY);
      builder.addInterceptor(interceptor);

      final Callable<Callable<String>> baseUrlAdapterFactory = configs.baseUrlAdapterFactory();
      if (baseUrlAdapterFactory != null) {
        try {
          builder.addInterceptor(new BaseUrlOverridingInterceptor(baseUrlAdapterFactory.call()));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return builder.build();
  }

  @Provides BookingResolver getBookingResolver() {
    return new BookingResolverImpl(
        context.getResources(),
        context.getPackageManager(),
        new AndroidGeocoder(context)
    );
  }

  @Provides TripUpdateApi getTripUpdateApi(Gson gson, OkHttpClient httpClient) {
    return new Retrofit.Builder()
        /* This base url is ignored as the api relies on @Url. */
        .baseUrl(Server.ApiTripGo.getValue())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient)
        .build()
        .create(TripUpdateApi.class);
  }

  @Singleton @Provides TripUpdater getTripUpdater(TripUpdateApi api, Gson gson) {
    return new TripUpdaterImpl(context.getResources(), api, gson);
  }

  @Provides LocationInfoApi getLocationInfoApi(Gson gson, OkHttpClient httpClient) {
    return new Retrofit.Builder()
        /* This base url is ignored as the api relies on @Url. */
        .baseUrl(Server.ApiTripGo.getValue())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient)
        .build()
        .create(LocationInfoApi.class);
  }

  @Provides LocationInfoService getLocationInfoService(
      LocationInfoApi locationInfoApi,
      RegionService regionService) {
    return new LocationInfoServiceImpl(locationInfoApi, regionService);
  }

  @Singleton @Provides Gson getGson() {
    return new GsonBuilder()
        .registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
        .registerTypeAdapterFactory(new GsonAdaptersRegionInfoBody())
        .registerTypeAdapterFactory(new GsonAdaptersRegionInfo())
        .registerTypeAdapterFactory(new GsonAdaptersRegionInfoResponse())
        .registerTypeAdapterFactory(new GsonAdaptersLocationInfo())
        .registerTypeAdapterFactory(new GsonAdaptersLocationInfoDetails())
        .registerTypeAdapterFactory(new GsonAdaptersCarPark())
        .registerTypeAdapterFactory(new GsonAdaptersBooking())
        .create();
  }

  @Singleton @Provides
  Consumer<Throwable> getErrorHandler() {
    final Consumer<Throwable> errorHandler = configs.errorHandler();
    return new Consumer<Throwable>() {
      @Override public void accept(Throwable error) throws Exception {
        if (configs.debuggable()) {
          Log.e(TripKit.class.getSimpleName(), error.getMessage(), error);
        }
        if (errorHandler != null) {
          errorHandler.accept(error);
        }
      }
    };
  }

  @Provides AppVersionNameRepository appVersionNameRepository(Context context) {
    return new AppVersionNameRepositoryImpl(context);
  }
}
