package com.skedgo.android.tripkit;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.android.common.model.GsonAdaptersBooking;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.TransportMode;
import com.skedgo.android.common.util.DiagnosticUtils;
import com.skedgo.android.common.util.Gsons;
import com.skedgo.android.common.util.LowercaseEnumTypeAdapterFactory;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static retrofit.RestAdapter.LogLevel.FULL;
import static retrofit.RestAdapter.LogLevel.NONE;

@Module
class MainModule {
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
    return new RestAdapter.Builder()
        .setLogLevel(configs.debuggable() ? FULL : NONE)
        .setEndpoint("https://tripgo.skedgo.com/satapp")
        .setConverter(new GsonConverter(Gsons.createForRegion()))
        .setClient(new OkClient(httpClient))
        .build()
        .create(RegionsApi.class);
  }

  @Singleton @Provides RegionDatabaseHelper getRegionDatabaseHelper() {
    return new RegionDatabaseHelper(
        context,
        "regions.db"
    );
  }

  @Singleton @Provides Func1<String, RegionInfoApi> getRegionInfoApiFactory(
      final Gson gson,
      final OkHttpClient httpClient) {
    return new Func1<String, RegionInfoApi>() {
      @Override public RegionInfoApi call(String endpoint) {
        return new RestAdapter.Builder()
            .setLogLevel(configs.debuggable() ? FULL : NONE)
            .setEndpoint(endpoint)
            .setConverter(new GsonConverter(gson))
            .setClient(new OkClient(httpClient))
            .build()
            .create(RegionInfoApi.class);
      }
    };
  }

  @Singleton @Provides RegionService getRegionService(
      RegionDatabaseHelper databaseHelper,
      RegionsApi regionsApi,
      Func1<String, RegionInfoApi> regionInfoApiFactory) {
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
        regionInfoApiFactory,
        regionsFetcher
    );
  }

  @Provides Func1<String, RoutingApi> getRoutingApiFactory(
      final Gson gson,
      final OkHttpClient httpClient) {
    return new Func1<String, RoutingApi>() {
      @Override public RoutingApi call(String endpoint) {
        return new RestAdapter.Builder()
            .setLogLevel(configs.debuggable() ? FULL : NONE)
            .setEndpoint(endpoint)
            .setConverter(new GsonConverter(gson))
            .setClient(new OkClient(httpClient))
            .build()
            .create(RoutingApi.class);
      }
    };
  }

  @Singleton @Provides RouteService getRouteService(
      Func1<String, RoutingApi> routingApiFactory,
      RegionService regionService,
      Gson gson) {
    Co2Preferences co2Preferences = null;
    final Func0<Co2Preferences> co2PreferencesFactory = configs.co2PreferencesFactory();
    if (co2PreferencesFactory != null) {
      co2Preferences = co2PreferencesFactory.call();
    }

    TripPreferences tripPreferences = null;
    final Func0<TripPreferences> tripPreferencesFactory = configs.tripPreferencesFactory();
    if (tripPreferencesFactory != null) {
      tripPreferences = tripPreferencesFactory.call();
    }

    final QueryGeneratorImpl queryGenerator = new QueryGeneratorImpl(regionService);
    return new RouteServiceImpl(
        context.getResources(),
        getAppVersion(),
        queryGenerator,
        routingApiFactory,
        configs.excludedTransitModesAdapter(),
        co2Preferences,
        tripPreferences,
        gson
    );
  }

  @Singleton @Provides OkHttpClient getOkHttpClient() {
    final OkHttpClient httpClient = new OkHttpClient();
    httpClient.interceptors().add(
        BuiltInInterceptorCompatBuilder.create()
            .appVersion(getAppVersion())
            .locale(Locale.getDefault())
            .regionEligibility(configs.regionEligibility())
            .userTokenProvider(configs.userTokenProvider())
            .build());
    if (configs.debuggable()) {
      final Func0<Func0<String>> baseUrlAdapterFactory = configs.baseUrlAdapterFactory();
      if (baseUrlAdapterFactory != null) {
        httpClient.interceptors().add(new BaseUrlOverridingInterceptorCompat(baseUrlAdapterFactory.call()));
      }
    }

    return httpClient;
  }

  @Provides HttpLoggingInterceptor getHttpLoggingInterceptor() {
    final HttpLoggingInterceptor.Level level = configs.debuggable()
        ? HttpLoggingInterceptor.Level.BODY
        : HttpLoggingInterceptor.Level.NONE;
    return new HttpLoggingInterceptor().setLevel(level);
  }

  @Provides BuiltInInterceptor builtInInterceptor(Lazy<UuidProvider> uuidProviderLazy) {
    final UuidProvider uuidProvider = configs.isUuidOptedOut() ? null : uuidProviderLazy.get();
    return BuiltInInterceptorBuilder.create()
        .appVersion(getAppVersion())
        .locale(Locale.getDefault())
        .regionEligibility(configs.regionEligibility())
        .userTokenProvider(configs.userTokenProvider())
        .uuidProvider(uuidProvider)
        .build();
  }

  @Singleton @Provides okhttp3.OkHttpClient getOkHttpClient3(
      BuiltInInterceptor builtInInterceptor,
      Provider<HttpLoggingInterceptor> httpLoggingInterceptorProvider) {
    final okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder()
        .addInterceptor(builtInInterceptor);
    if (configs.debuggable()) {
      final Func0<Func0<String>> baseUrlAdapterFactory = configs.baseUrlAdapterFactory();
      if (baseUrlAdapterFactory != null) {
        builder.addInterceptor(new BaseUrlOverridingInterceptor(baseUrlAdapterFactory.call()));
      }

      builder.addInterceptor(httpLoggingInterceptorProvider.get());
    }

    return builder.build();
  }

  @Provides ServiceExtrasService getServiceExtrasService(
      Gson gson,
      OkHttpClient httpClient,
      RegionService regionService) {
    final DynamicEndpoint endpoint = new AlphaDynamicEndpoint(
        "https://tripgo.skedgo.com/satapp",
        "skedgo"
    );
    final ServiceApi serviceApi = new RestAdapter.Builder()
        .setLogLevel(configs.debuggable() ? FULL : NONE)
        .setEndpoint(endpoint)
        .setConverter(new GsonConverter(gson))
        .setClient(new OkClient(httpClient))
        .build()
        .create(ServiceApi.class);
    return new AlphaServiceExtrasService(
        regionService,
        endpoint,
        serviceApi
    );
  }

  @Provides Func1<String, ReportingApi> getReportingApiFactory(
      final Gson gson,
      final OkHttpClient httpClient) {
    return new Func1<String, ReportingApi>() {
      @Override public ReportingApi call(String endpoint) {
        return new RestAdapter.Builder()
            .setLogLevel(configs.debuggable() ? FULL : NONE)
            .setEndpoint(endpoint)
            .setConverter(new GsonConverter(gson))
            .setClient(new OkClient(httpClient))
            .build()
            .create(ReportingApi.class);
      }
    };
  }

  @Singleton @Provides Reporter getReporter(
      Func1<String, ReportingApi> reportingApiFactory,
      Action1<Throwable> errorHandler) {
    return new ReporterImpl(reportingApiFactory, errorHandler);
  }

  @Provides BookingResolver getBookingResolver() {
    return new BookingResolverImpl(
        context.getResources(),
        context.getPackageManager(),
        new SingleReverseGeocoderFactory(context)
    );
  }

  @Provides TripUpdateApi getTripUpdateApi(Gson gson, okhttp3.OkHttpClient httpClient) {
    return new Retrofit.Builder()
        /* This base url is ignored as the api relies on @Url. */
        .baseUrl(HttpUrl.parse("https://tripgo.skedgo.com/satapp/"))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient)
        .build()
        .create(TripUpdateApi.class);
  }

  @Singleton @Provides TripUpdater getTripUpdater(TripUpdateApi api, Gson gson) {
    return new TripUpdaterImpl(context.getResources(), api, "12", gson);
  }

  @Provides LocationInfoApi getLocationInfoApi(Gson gson, okhttp3.OkHttpClient httpClient) {
    return new Retrofit.Builder()
        /* This base url is ignored as the api relies on @Url. */
        .baseUrl(HttpUrl.parse("https://tripgo.skedgo.com/satapp/"))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
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
        .registerTypeAdapterFactory(new GsonAdaptersRegionInfo())
        .registerTypeAdapterFactory(new GsonAdaptersRegionInfoResponse())
        .registerTypeAdapterFactory(new GsonAdaptersLocationInfo())
        .registerTypeAdapterFactory(new GsonAdaptersLocationInfoDetails())
        .registerTypeAdapterFactory(new GsonAdaptersBooking())
        .create();
  }

  @NonNull String getAppVersion() {
    return "a-" + configs.regionEligibility() + DiagnosticUtils.getAppVersionName(context);
  }

  @Singleton @Provides Action1<Throwable> getErrorHandler() {
    final Action1<Throwable> errorHandler = configs.errorHandler();
    return new Action1<Throwable>() {
      @Override public void call(Throwable error) {
        if (configs.debuggable()) {
          Log.e(TripKit.class.getSimpleName(), error.getMessage(), error);
        }
        if (errorHandler != null) {
          errorHandler.call(error);
        }
      }
    };
  }
}