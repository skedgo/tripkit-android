package com.skedgo.android.tripkit;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.functions.Func0;

/**
 * We'll migrate to {@link HttpClientModule} soon.
 */
@Deprecated
@Module
public class HttpClient2Module {
  @Deprecated @Singleton @Provides public OkHttpClient httpClient2(
      Configs configs,
      BuiltInInterceptorCompat builtInInterceptorCompat) {
    final OkHttpClient httpClient = new OkHttpClient();
    httpClient.interceptors().add(builtInInterceptorCompat);

    if (configs.debuggable()) {
      final Func0<Func0<String>> baseUrlAdapterFactory = configs.baseUrlAdapterFactory();
      if (baseUrlAdapterFactory != null) {
        httpClient.interceptors().add(new BaseUrlOverridingInterceptorCompat(baseUrlAdapterFactory.call()));
      }
    }

    return httpClient;
  }
}