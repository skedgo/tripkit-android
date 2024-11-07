package com.skedgo.tripkit.tsp;

import com.google.gson.Gson;
import com.skedgo.tripkit.configuration.ServerManager;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class TspModule {
    @Provides
    RegionInfoApi regionInfoApi(
        Gson gson,
        okhttp3.OkHttpClient httpClient) {
        return new Retrofit.Builder()
            .baseUrl(ServerManager.INSTANCE.getConfiguration().getApiTripGoUrl())
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
            .create(RegionInfoApi.class);
    }
}