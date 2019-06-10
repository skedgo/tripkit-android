package skedgo.tripkit.timetable

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers.io
import skedgo.tripkit.configuration.Server
import javax.inject.Singleton

@Module
class DeparturesModule {

  @Provides
  @Singleton
  fun departuresRepository(impl: DeparturesRepositoryImpl): DeparturesRepository = impl

  @Provides
  internal fun departuresApi(
      gson: Gson,
      httpClient: OkHttpClient
  ): DeparturesApi = Retrofit.Builder()
      /* This base url is ignored as the api relies on @Url. */
      .baseUrl(Server.ApiTripGo.value)
      .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(io()))
      .addConverterFactory(GsonConverterFactory.create(gson))
      .client(httpClient)
      .build()
      .create(DeparturesApi::class.java)
}