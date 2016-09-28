package com.skedgo.android.tripkit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class HttpClientModule {
  @Provides public OkHttpClient.Builder httpClientBuilder() {
    return new OkHttpClient.Builder();
  }
}