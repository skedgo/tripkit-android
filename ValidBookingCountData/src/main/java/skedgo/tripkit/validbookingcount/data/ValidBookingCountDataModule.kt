package skedgo.tripkit.validbookingcount.data

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import skedgo.tripkit.configuration.Server
import skedgo.tripkit.validbookingcount.domain.ValidBookingCountRepository

@Module
class ValidBookingCountDataModule {
  @Provides fun validBookingCountRepository(
      httpClient: OkHttpClient,
      context: Context
  ): ValidBookingCountRepository {
    val api = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(Server.Inflationary.value)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ValidBookingCountApi::class.java)
    val preferences = context.getSharedPreferences(
        "ValidBookingCountPreferences",
        Context.MODE_PRIVATE
    )
    return ValidBookingCountRepositoryImpl(api, preferences)
  }
}
