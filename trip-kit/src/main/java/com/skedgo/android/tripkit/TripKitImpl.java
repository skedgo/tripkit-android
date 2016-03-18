package com.skedgo.android.tripkit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.skedgo.android.common.model.Query;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.TransportMode;
import com.skedgo.android.common.util.DiagnosticUtils;
import com.skedgo.android.common.util.Gsons;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static retrofit.RestAdapter.LogLevel.FULL;
import static retrofit.RestAdapter.LogLevel.NONE;

final class TripKitImpl extends TripKit {
  private final Context context;
  private final Configs configs;
  private final Action1<Throwable> errorHandler;
  private OkHttpClient okHttpClient; // Lazily initialized.
  private RegionService regionService; // Lazily initialized.
  private RegionDatabaseHelper regionDatabaseHelper; // Lazily initialized.
  private RouteService routeService; // Lazily initialized.
  private Reporter reporter; // Lazily initialized.
  private TripUpdater tripUpdater; // Lazily initialized.
  private InterAppCommunicator interAppCommunicator; // Lazily initialized.

  TripKitImpl(@NonNull Configs configs) {
    this.configs = configs;
    context = configs.context().getApplicationContext();
    errorHandler = createLoggableErrorHandler(configs.errorHandler());
  }

  @Override
  public synchronized RouteService getRouteService() {
    if (routeService == null) {
      routeService = new RouteServiceImpl(
          context.getResources(),
          getAppVersion(),
          provideQueryGenerator(),
          createRoutingApiFactory(),
          configs.excludedTransitModesAdapter());
    }

    return routeService;
  }

  @Override
  public ServiceExtrasService getServiceExtrasService() {
    final DynamicEndpoint endpoint = provideDynamicEndpoint();
    final ServiceApi serviceApi = new RestAdapter.Builder()
        .setLogLevel(configs.debuggable() ? FULL : NONE)
        .setEndpoint(endpoint)
        .setConverter(new GsonConverter(GsonProvider.get()))
        .setClient(new OkClient(getOkHttpClient()))
        .build()
        .create(ServiceApi.class);
    return new AlphaServiceExtrasService(
        getRegionService(),
        endpoint,
        serviceApi
    );
  }

  @Override public synchronized Reporter getReporter() {
    if (reporter == null) {
      reporter = new ReporterImpl(createReportingApiFactory(), getErrorHandler());
    }

    return reporter;
  }

  @Override public synchronized InterAppCommunicator getInterAppCommunicator() {
    if (interAppCommunicator == null) {
      interAppCommunicator = new InterAppCommunicatorImpl(context.getResources(), null, null);
    }

    return interAppCommunicator;
  }

  @Override
  public synchronized RegionService getRegionService() {
    if (regionService == null) {
      final RegionDatabaseHelper databaseHelper = getRegionDatabaseHelper();
      final RegionsFetcher regionsFetcher = new RegionsFetcherImpl(
          getRegionsApi(),
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
      regionService = new RegionServiceImpl(
          regionCache,
          modeCache,
          createRegionInfoApiFactory(),
          regionsFetcher
      );
    }

    return regionService;
  }

  @Override
  public synchronized OkHttpClient getOkHttpClient() {
    if (okHttpClient == null) {
      final OkHttpClient okHttpClient = new OkHttpClient();
      okHttpClient.interceptors().add(addCustomRequestHeaders());
      this.okHttpClient = okHttpClient;
    }

    return okHttpClient;
  }

  @Override synchronized TripUpdater getTripUpdater() {
    if (tripUpdater == null) {
      final TripUpdateApi api = new Retrofit.Builder()
          /* This base url is ignored as the api relies on @Url. */
          .baseUrl(HttpUrl.parse("https://tripgo.skedgo.com/satapp/"))
          .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create(GsonProvider.get()))
          .client(getOkHttpClient3())
          .build()
          .create(TripUpdateApi.class);
      tripUpdater = new TripUpdaterImpl(context.getResources(), api, "11");
    }

    return tripUpdater;
  }

  RegionsApi getRegionsApi() {
    return new RestAdapter.Builder()
        .setLogLevel(configs.debuggable() ? FULL : NONE)
        .setEndpoint("https://tripgo.skedgo.com/satapp")
        .setConverter(new GsonConverter(Gsons.createForRegion()))
        .setClient(new OkClient(getOkHttpClient()))
        .build()
        .create(RegionsApi.class);
  }

  synchronized RegionDatabaseHelper getRegionDatabaseHelper() {
    if (regionDatabaseHelper == null) {
      regionDatabaseHelper = new RegionDatabaseHelper(
          context,
          "regions.db"
      );
    }

    return regionDatabaseHelper;
  }

  @Override Action1<Throwable> getErrorHandler() {
    return errorHandler;
  }

  @NonNull Func1<Query, Observable<List<Query>>> provideQueryGenerator() {
    return new QueryGeneratorImpl(
        getRegionService()
    );
  }

  @NonNull DynamicEndpoint provideDynamicEndpoint() {
    return new AlphaDynamicEndpoint(
        "https://tripgo.skedgo.com/satapp",
        "skedgo"
    );
  }

  @NonNull HttpLoggingInterceptor createHttpLoggingInterceptor() {
    final HttpLoggingInterceptor.Level level = configs.debuggable()
        ? HttpLoggingInterceptor.Level.BODY
        : HttpLoggingInterceptor.Level.NONE;
    return new HttpLoggingInterceptor().setLevel(level);
  }

  @NonNull String getAppVersion() {
    return "a-" + configs.regionEligibility() + DiagnosticUtils.getAppVersionName(context);
  }

  @NonNull private okhttp3.OkHttpClient getOkHttpClient3() {
    return new okhttp3.OkHttpClient.Builder()
        .addInterceptor(
            BuiltInInterceptor.builder()
                .appVersion(getAppVersion())
                .locale(Locale.getDefault())
                .regionEligibility(configs.regionEligibility())
                .build()
        )
        .addInterceptor(createHttpLoggingInterceptor())
        .build();
  }

  @NonNull private Interceptor addCustomRequestHeaders() {
    return Interceptors.addCustomRequestHeaders(
        getAppVersion(),
        configs.regionEligibility(),
        Locale.getDefault()
    );
  }

  @NonNull private Func1<String, ReportingApi> createReportingApiFactory() {
    return new Func1<String, ReportingApi>() {
      @Override public ReportingApi call(String endpoint) {
        return new RestAdapter.Builder()
            .setLogLevel(configs.debuggable() ? FULL : NONE)
            .setEndpoint(endpoint)
            .setConverter(new GsonConverter(GsonProvider.get()))
            .setClient(new OkClient(getOkHttpClient()))
            .build()
            .create(ReportingApi.class);
      }
    };
  }

  @NonNull private Func1<String, RoutingApi> createRoutingApiFactory() {
    return new Func1<String, RoutingApi>() {
      @Override public RoutingApi call(String endpoint) {
        return new RestAdapter.Builder()
            .setLogLevel(configs.debuggable() ? FULL : NONE)
            .setEndpoint(endpoint)
            .setConverter(new GsonConverter(GsonProvider.get()))
            .setClient(new OkClient(getOkHttpClient()))
            .build()
            .create(RoutingApi.class);
      }
    };
  }

  @NonNull private Func1<String, RegionInfoApi> createRegionInfoApiFactory() {
    return new Func1<String, RegionInfoApi>() {
      @Override public RegionInfoApi call(String endpoint) {
        return new RestAdapter.Builder()
            .setLogLevel(configs.debuggable() ? FULL : NONE)
            .setEndpoint(endpoint)
            .setConverter(new GsonConverter(GsonProvider.get()))
            .setClient(new OkClient(getOkHttpClient()))
            .build()
            .create(RegionInfoApi.class);
      }
    };
  }

  @NonNull private Action1<Throwable> createLoggableErrorHandler(
      @Nullable final Action1<Throwable> errorHandler) {
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