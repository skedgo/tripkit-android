package com.skedgo.android.tripkit

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers

@Module
class AnalyticsDataModule {
  @Provides fun markTripAsPlannedWithUserInfo(
      markTripAsPlannedWithUserInfoImpl: MarkTripAsPlannedWithUserInfoImpl
  ): MarkTripAsPlannedWithUserInfo = markTripAsPlannedWithUserInfoImpl

  @Provides internal fun reportingApi(gson: Gson, httpClient: OkHttpClient): ReportingApi
      = Retrofit.Builder()
      /* This base url is ignored as the api relies on @Url. */
      .baseUrl("https://tripgo.skedgo.com/satapp/")
      .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
      .addConverterFactory(GsonConverterFactory.create(gson))
      .client(httpClient)
      .build()
      .create(ReportingApi::class.java)
}
