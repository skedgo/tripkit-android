package com.skedgo.tripkit.a2brouting

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import io.reactivex.schedulers.Schedulers
import com.skedgo.tripkit.configuration.Server
import java.util.concurrent.TimeUnit

@Module
class A2bRoutingDataModule {
  @Provides internal fun a2bRoutingApi(gson: Gson, httpClient: OkHttpClient): A2bRoutingApi {
    val client = httpClient
        .newBuilder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(1, TimeUnit.SECONDS)
        .build()
    return Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(Server.ApiTripGo.value)
        .client(client)
        .build()
        .create(A2bRoutingApi::class.java)
  }

  @Provides internal fun failoverA2bRoutingApi(
          configs: com.skedgo.tripkit.Configs,
          gson: Gson,
          a2bRoutingApi: A2bRoutingApi
  ): FailoverA2bRoutingApi = FailoverA2bRoutingApi(
          configs.context().resources,
          gson,
          a2bRoutingApi
  )
}