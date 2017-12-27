package com.skedgo.android.tripkit.tsp;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import skedgo.tripkit.configuration.Server;

import static rx.schedulers.Schedulers.io;

@Module
public class TspModule {
  @Provides RegionInfoApi regionInfoApi(
      Gson gson,
      okhttp3.OkHttpClient httpClient) {
    return new Retrofit.Builder()
        .baseUrl(Server.ApiTripGo.getValue())
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(io()))
        .build()
        .create(RegionInfoApi.class);
  }
}