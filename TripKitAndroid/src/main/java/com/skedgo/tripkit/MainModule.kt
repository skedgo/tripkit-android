package com.skedgo.tripkit

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.skedgo.TripKit
import com.skedgo.tripkit.a2brouting.FailoverA2bRoutingApi
import com.skedgo.tripkit.a2brouting.RouteService
import com.skedgo.tripkit.bookingproviders.BookingResolver
import com.skedgo.tripkit.bookingproviders.BookingResolverImpl
import com.skedgo.tripkit.common.model.TransportMode
import com.skedgo.tripkit.common.model.booking.GsonAdaptersBooking
import com.skedgo.tripkit.common.model.region.Region
import com.skedgo.tripkit.common.util.Gsons
import com.skedgo.tripkit.common.util.LowercaseEnumTypeAdapterFactory
import com.skedgo.tripkit.configuration.ServerManager
import com.skedgo.tripkit.data.regions.RegionService
import com.skedgo.tripkit.data.tsp.GsonAdaptersRegionInfo
import com.skedgo.tripkit.tsp.GsonAdaptersRegionInfoBody
import com.skedgo.tripkit.tsp.GsonAdaptersRegionInfoResponse
import com.skedgo.tripkit.tsp.RegionInfoRepository
import dagger.Module
import dagger.Provides
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class MainModule(private val configs: Configs) {

    private val context: Context = configs.context().applicationContext

    @Provides
    fun configs(): Configs = configs

    @Provides
    fun getRegionsApi(httpClient: OkHttpClient): RegionsApi {
        return Retrofit.Builder()
            .baseUrl(ServerManager.configuration.apiTripGoUrl)
            .addConverterFactory(GsonConverterFactory.create(Gsons.createForRegion()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(httpClient)
            .build()
            .create(RegionsApi::class.java)
    }

    @Singleton
    @Provides
    fun getRegionDatabaseHelper(): RegionDatabaseHelper {
        return RegionDatabaseHelper(context, "regions.db")
    }

    @Singleton
    @Provides
    fun getRegionService(
        databaseHelper: RegionDatabaseHelper,
        regionsApi: RegionsApi,
        regionInfoRepository: RegionInfoRepository
    ): RegionService {
        val regionsFetcher = RegionsFetcherImpl(regionsApi, databaseHelper)
        val regionCache: Cache<List<Region>> = CacheImpl(
            regionsFetcher.fetchAsync(),
            databaseHelper.loadRegionsAsync()
        )
        val modeCache: Cache<Map<String, TransportMode>> = CacheImpl(
            regionsFetcher.fetchAsync(),
            databaseHelper.loadModesAsync()
        )
        return RegionServiceImpl(
            regionCache,
            modeCache,
            regionsFetcher,
            regionInfoRepository,
            RegionFinder()
        )
    }

    @Singleton
    @Provides
    fun routeService(
        routingApi: FailoverA2bRoutingApi,
        regionService: RegionService,
        configs: Configs,
        regionInfoRepository: RegionInfoRepository
    ): RouteService {
        val co2Preferences: Co2Preferences? = try {
            configs.co2PreferencesFactory()?.call()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        val tripPreferences: TripPreferences? = try {
            configs.tripPreferencesFactory()?.call()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        val queryGenerator = QueryGeneratorImpl(regionService)
        return RouteServiceImpl(
            context,
            queryGenerator,
            co2Preferences,
            tripPreferences,
            configs.extraQueryMapProvider(),
            routingApi,
            regionInfoRepository
        )
    }

    @Provides
    fun context(): Context = configs.context()

    @Provides
    fun getBookingResolver(): BookingResolver {
        return BookingResolverImpl(
            context.resources,
            context.packageManager,
            AndroidGeocoder(context)
        )
    }

    @Provides
    fun getLocationInfoApi(gson: Gson, httpClient: OkHttpClient): LocationInfoApi {
        return Retrofit.Builder()
            .baseUrl(ServerManager.configuration.apiTripGoUrl) // Ignored base URL
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
            .create(LocationInfoApi::class.java)
    }

    @Provides
    fun getLocationInfoService(
        locationInfoApi: LocationInfoApi,
        regionService: RegionService
    ): LocationInfoService {
        return LocationInfoServiceImpl(locationInfoApi, regionService)
    }

    @Singleton
    @Provides
    fun getGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapterFactory(LowercaseEnumTypeAdapterFactory())
            .registerTypeAdapterFactory(GsonAdaptersRegionInfoBody())
            .registerTypeAdapterFactory(GsonAdaptersRegionInfo())
            .registerTypeAdapterFactory(GsonAdaptersRegionInfoResponse())
            .registerTypeAdapterFactory(GsonAdaptersLocationInfo())
            .registerTypeAdapterFactory(GsonAdaptersLocationInfoDetails())
            .registerTypeAdapterFactory(GsonAdaptersCarPark())
            .registerTypeAdapterFactory(GsonAdaptersBooking())
            .create()
    }

    @Singleton
    @Provides
    fun getErrorHandler(): Consumer<Throwable> {
        val errorHandler = configs.errorHandler()
        return Consumer { error ->
            if (configs.debuggable()) {
                Log.e(TripKit::class.java.simpleName, error.message, error)
            }
            errorHandler?.accept(error)
        }
    }
}
