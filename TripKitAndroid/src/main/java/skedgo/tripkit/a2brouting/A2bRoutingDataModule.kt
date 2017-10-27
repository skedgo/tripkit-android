package skedgo.tripkit.a2brouting

import com.google.gson.Gson
import com.skedgo.android.tripkit.Configs
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import skedgo.tripkit.configuration.Server
import java.util.concurrent.TimeUnit

@Module
class A2bRoutingDataModule {
  @Provides internal fun a2bRoutingApi(gson: Gson, httpClient: OkHttpClient): A2bRoutingApi
      = Retrofit.Builder()
      .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
      .addConverterFactory(GsonConverterFactory.create(gson))
      .baseUrl(Server.Inflationary.value)
      .client(httpClient
          .newBuilder()
          .readTimeout(30, TimeUnit.SECONDS)
          .connectTimeout(1, TimeUnit.SECONDS)
          .build()
      )
      .build()
      .create(A2bRoutingApi::class.java)

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