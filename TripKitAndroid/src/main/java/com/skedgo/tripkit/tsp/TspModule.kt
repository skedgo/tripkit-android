package com.skedgo.tripkit.tsp

import com.google.gson.Gson
import com.skedgo.tripkit.configuration.ServerManager.configuration
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit.Builder
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class TspModule {
    @Provides
    fun regionInfoApi(
        gson: Gson,
        httpClient: OkHttpClient
    ): RegionInfoApi {
        return Builder()
            .baseUrl(configuration.apiTripGoUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
            .create(RegionInfoApi::class.java)
    }
}