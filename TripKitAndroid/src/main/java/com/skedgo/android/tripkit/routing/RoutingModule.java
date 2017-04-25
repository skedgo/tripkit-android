package com.skedgo.android.tripkit.routing;

import com.google.gson.Gson;
import com.skedgo.android.tripkit.Configs;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;
import skedgo.tripkit.a2brouting.FailoverRoutingApi;
import skedgo.tripkit.a2brouting.RoutingApi;

@Module
public class RoutingModule {
  @Provides RoutingApi routingApi(Gson gson, OkHttpClient httpClient) {
    return new Retrofit.Builder()
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("https://tripgo.skedgo.com/satapp/")
        .client(httpClient)
        .build()
        .create(RoutingApi.class);
  }

  @Provides FailoverRoutingApi failoverRoutingApi(
      Configs configs,
      Gson gson,
      RoutingApi routingApi
  ) {
    return new FailoverRoutingApi(
        configs.context().getResources(),
        gson,
        routingApi
    );
  }
}