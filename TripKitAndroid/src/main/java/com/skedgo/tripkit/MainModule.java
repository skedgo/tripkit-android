package com.skedgo.tripkit;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.TripKit;
import com.skedgo.tripkit.a2brouting.FailoverA2bRoutingApi;
import com.skedgo.tripkit.a2brouting.RouteService;
import com.skedgo.tripkit.bookingproviders.BookingResolver;
import com.skedgo.tripkit.bookingproviders.BookingResolverImpl;
import com.skedgo.tripkit.common.model.GsonAdaptersBooking;
import com.skedgo.tripkit.common.model.Region;
import com.skedgo.tripkit.common.model.TransportMode;
import com.skedgo.tripkit.common.util.Gsons;
import com.skedgo.tripkit.common.util.LowercaseEnumTypeAdapterFactory;
import com.skedgo.tripkit.configuration.ServerManager;
import com.skedgo.tripkit.data.regions.RegionService;
import com.skedgo.tripkit.data.tsp.GsonAdaptersRegionInfo;
import com.skedgo.tripkit.tsp.GsonAdaptersRegionInfoBody;
import com.skedgo.tripkit.tsp.GsonAdaptersRegionInfoResponse;
import com.skedgo.tripkit.tsp.RegionInfoRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MainModule {
    private final Configs configs;
    private final Context context;

    public MainModule(@NonNull Configs configs) {
        this.configs = configs;
        context = configs.context().getApplicationContext();
    }

    @Provides
    Configs configs() {
        return configs;
    }

    @Provides
    RegionsApi getRegionsApi(OkHttpClient httpClient) {
        return new Retrofit.Builder()
            .baseUrl(ServerManager.INSTANCE.getConfiguration().getApiTripGoUrl())
            .addConverterFactory(GsonConverterFactory.create(Gsons.createForRegion()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(httpClient)
            .build()
            .create(RegionsApi.class);
    }

    @Singleton
    @Provides
    RegionDatabaseHelper getRegionDatabaseHelper() {
        return new RegionDatabaseHelper(
            context,
            "regions.db"
        );
    }

    @Singleton
    @Provides
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

    @Singleton
    @Provides
    RouteService routeService(
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

    @Provides
    Context context() {
        return configs.context();
    }

    @Provides
    BookingResolver getBookingResolver() {
        return new BookingResolverImpl(
            context.getResources(),
            context.getPackageManager(),
            new AndroidGeocoder(context)
        );
    }


    @Provides
    LocationInfoApi getLocationInfoApi(Gson gson, OkHttpClient httpClient) {
        return new Retrofit.Builder()
            /* This base url is ignored as the api relies on @Url. */
            .baseUrl(ServerManager.INSTANCE.getConfiguration().getApiTripGoUrl())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
            .create(LocationInfoApi.class);
    }

    @Provides
    LocationInfoService getLocationInfoService(
        LocationInfoApi locationInfoApi,
        RegionService regionService) {
        return new LocationInfoServiceImpl(locationInfoApi, regionService);
    }

    @Singleton
    @Provides
    Gson getGson() {
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

    @Singleton
    @Provides
    Consumer<Throwable> getErrorHandler() {
        final Consumer<Throwable> errorHandler = configs.errorHandler();
        return error -> {
            if (configs.debuggable()) {
                Log.e(TripKit.class.getSimpleName(), error.getMessage(), error);
            }
            if (errorHandler != null) {
                errorHandler.accept(error);
            }
        };
    }
}
