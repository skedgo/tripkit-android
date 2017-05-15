package skedgo.tripkit.a2brouting

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.skedgo.android.tripkit.Configs
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import skedgo.tripkit.configuration.Server
import skedgo.tripkit.routing.GsonAdaptersProvider
import skedgo.tripkit.routing.GsonAdaptersSource

@Module
class A2bRoutingDataModule {
  @Provides internal fun a2bRoutingApi(httpClient: OkHttpClient): A2bRoutingApi {
    val gson = GsonBuilder()
        .registerTypeAdapterFactory(GsonAdaptersSource())
        .registerTypeAdapterFactory(GsonAdaptersProvider())
        .create()
    return Retrofit.Builder()
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(Server.Inflationary.value)
        .client(httpClient)
        .build()
        .create(A2bRoutingApi::class.java)
  }

  @Provides internal fun failoverA2bRoutingApi(
      configs: Configs,
      gson: Gson,
      a2bRoutingApi: A2bRoutingApi
  ): FailoverA2bRoutingApi = FailoverA2bRoutingApi(
      configs.context().resources,
      gson,
      a2bRoutingApi
  )
}