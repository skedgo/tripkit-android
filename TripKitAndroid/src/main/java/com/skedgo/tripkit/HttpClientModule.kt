package com.skedgo.tripkit

import android.content.Context
import android.content.SharedPreferences
import android.webkit.URLUtil
import com.google.gson.Gson
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.skedgo.tripkit.configuration.AppVersionNameRepository
import com.skedgo.tripkit.configuration.GetAppVersion
import com.skedgo.tripkit.configuration.Server
import com.skedgo.tripkit.data.HttpClientCustomDataStore
import com.skedgo.tripkit.regionrouting.GetRegionRoutingService
import com.skedgo.tripkit.regionrouting.RegionRoutingApi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * @suppress
 */
@Module
open class HttpClientModule(
        private val buildFlavor: String?,
        private val version: String?,
        private val configs: Configs,
        private val sharedPreferences: SharedPreferences? = null
) {

    @Singleton
    @Provides
    open fun httpClient(addCustomHeaders: AddCustomHeaders): OkHttpClient {
        val builder = httpClientBuilder()
                .addInterceptor(addCustomHeaders)
        if (configs.debuggable()) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }
        if (configs.baseUrlAdapterFactory() != null) {
            try {
                builder.addInterceptor(BaseUrlOverridingInterceptor(configs.baseUrlAdapterFactory()!!))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return builder.build()
    }

    @Provides
    open fun httpClientBuilder(): OkHttpClient.Builder {
        val dispatcher = Dispatcher().apply { maxRequestsPerHost = 15 }
        val builder = OkHttpClient.Builder().dispatcher(dispatcher)
        if (buildFlavor != null && version != null) {
            builder.addInterceptor(AddCustomUserAgent(buildFlavor, version))
        }
        builder.connectTimeout(30, TimeUnit.SECONDS)
        builder.readTimeout(30, TimeUnit.SECONDS)
        return builder
    }


    @Provides
    open fun appVersionNameRepository(context: Context): AppVersionNameRepository {
        return AppVersionNameRepositoryImpl(context)
    }

    /**
     * @return A [SharedPreferences] that contains
     * internal persistent configs (e.g. UUID) for TripKit.
     */
    @Provides
    @Named("TripKitPrefs")
    open fun preferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("TripKit", Context.MODE_PRIVATE)
    }

    @Provides
    internal fun addCustomHeaders(
            getAppVersion: GetAppVersion,
            uuidProviderLazy: Lazy<com.skedgo.tripkit.UuidProvider>
    ): AddCustomHeaders {
        return AddCustomHeaders(
                getAppVersion,
                { Locale.getDefault() },
                uuidProviderLazy.get(),
                configs.userTokenProvider(),
                { configs.key().call() },
                sharedPreferences
        )
    }

    @Provides
    open fun retrofitBuilder(gson: Gson): Retrofit.Builder {
        val customUrl = HttpClientCustomDataStore.getCustomBaseUrl()
        val baseUrl = if (customUrl != null && URLUtil.isValidUrl(customUrl)) {
            customUrl
        } else {
            Server.ApiTripGo.value
        }

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(NetworkResponseAdapterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Provides
    open fun getTripUpdateApi(builder: Retrofit.Builder, httpClient: OkHttpClient): TripUpdateApi {
        return builder
                .client(httpClient)
                .build()
                .create(TripUpdateApi::class.java)
    }

    @Singleton
    @Provides
    open fun getTripUpdater(context: Context, api: TripUpdateApi, gson: Gson): TripUpdater {
        return TripUpdaterImpl(context.resources, api, gson)
    }

    @Provides
    open fun getRegionRoutingApi(builder: Retrofit.Builder, httpClient: OkHttpClient): RegionRoutingApi {
        return builder
                .client(httpClient)
                .build()
                .create(RegionRoutingApi::class.java)
    }

    @Singleton
    @Provides
    open fun getRegionRoutingService(api: RegionRoutingApi): GetRegionRoutingService {
        return GetRegionRoutingService.GetRegionRoutingServiceImpl(api)
    }

}
