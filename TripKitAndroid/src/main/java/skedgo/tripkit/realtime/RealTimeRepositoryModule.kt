package skedgo.tripkit.realtime

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import skedgo.tripkit.configuration.Server
import javax.inject.Singleton

@Module
class RealTimeRepositoryModule {

  @Provides
  @Singleton
  fun realTimeAlertRepository(impl: RealtimeAlertRepositoryImpl): RealtimeAlertRepository = impl

  @Provides
  @Singleton
  fun realTimeRepository(impl: RealTimeRepositoryImpl): RealTimeRepository = impl

  @Provides
  internal fun latestApi(
      gson: Gson,
      httpClient: OkHttpClient
  ): LatestApi = Retrofit.Builder()
      /* This base url is ignored as the api relies on @Url. */
      .baseUrl(Server.ApiTripGo.value)
      .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
      .addConverterFactory(GsonConverterFactory.create(gson))
      .client(httpClient)
      .build()
      .create(LatestApi::class.java)
}