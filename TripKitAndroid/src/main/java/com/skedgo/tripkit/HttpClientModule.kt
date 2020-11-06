package com.skedgo.tripkit

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.google.gson.Gson
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.skedgo.tripkit.configuration.AppVersionNameRepository
import com.skedgo.tripkit.configuration.GetAppVersion
import com.skedgo.tripkit.configuration.Server
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

/**
 * @suppress
 */
@Module
open class HttpClientModule(private val buildFlavor: String?,
                            private val version: String?,
                            private val configs: Configs) {

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
    val builder = OkHttpClient.Builder()
    if (buildFlavor != null && version != null) {
      builder.addInterceptor(AddCustomUserAgent(buildFlavor, version))
    }
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

  @Provides internal fun addCustomHeaders(
          getAppVersion: GetAppVersion,
          uuidProviderLazy: Lazy<com.skedgo.tripkit.UuidProvider>
  ): AddCustomHeaders
      = AddCustomHeaders(
          getAppVersion,
          { Locale.getDefault() },
          if (configs.isUuidOptedOut) null else uuidProviderLazy.get(),
          configs.userTokenProvider(),
          { configs.key().call() })

  @Provides
  open fun retrofitBuilder(gson: Gson): Retrofit.Builder = Retrofit.Builder()
          .baseUrl(Server.ApiTripGo.value)
          .addCallAdapterFactory(NetworkResponseAdapterFactory())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create(gson))

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


}
