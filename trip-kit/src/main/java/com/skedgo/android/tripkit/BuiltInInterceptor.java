package com.skedgo.android.tripkit;

import android.support.annotation.Nullable;

import org.immutables.builder.Builder;
import org.immutables.value.Value;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.functions.Func0;
import skedgo.tripkit.configuration.ApiKey;

@Value.Style(newBuilder = "create")
final class BuiltInInterceptor implements Interceptor {
  private static final String HEADER_APP_VERSION = "X-TripGo-Version";
  private static final String HEADER_UUID = "X-TripGo-UUID";
  private static final String HEADER_API_KEY = "X-TripGo-Key";
  private static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
  private static final String HEADER_APPLICATION_JSON = "application/json";
  private static final String HEADER_ACCEPT = "Accept";
  private static final String HEADER_USER_TOKEN = "userToken";

  private final String appVersion;
  private final Locale locale;
  @Nullable private final Func0<String> uuidProvider;
  @Nullable private final Func0<String> userTokenProvider;
  private final Func0<ApiKey> getApiKey;

  private BuiltInInterceptor(
      String appVersion,
      Locale locale,
      @Nullable Func0<String> uuidProvider,
      @Nullable Func0<String> userTokenProvider,
      Func0<ApiKey> getApiKey
  ) {
    this.appVersion = appVersion;
    this.locale = locale;
    this.uuidProvider = uuidProvider;
    this.userTokenProvider = userTokenProvider;
    this.getApiKey = getApiKey;
  }

  @Builder.Factory
  static BuiltInInterceptor builtInInterceptor(
      String appVersion,
      Locale locale,
      @Nullable Func0<String> uuidProvider,
      @Nullable Func0<String> userTokenProvider,
      Func0<ApiKey> getApiKey
  ) {
    return new BuiltInInterceptor(
        appVersion,
        locale,
        uuidProvider,
        userTokenProvider,
        getApiKey
    );
  }

  @Override public Response intercept(Chain chain) throws IOException {
    final Request.Builder builder = chain.request().newBuilder()
        .addHeader(HEADER_ACCEPT_LANGUAGE, locale.getLanguage())
        .addHeader(HEADER_APP_VERSION, appVersion)
        .addHeader(HEADER_ACCEPT, HEADER_APPLICATION_JSON)
        .addHeader(HEADER_API_KEY, getApiKey.call().getValue());
    if (userTokenProvider != null && userTokenProvider.call() != null) {
      builder.addHeader(HEADER_USER_TOKEN, userTokenProvider.call());
    }
    if (uuidProvider != null) {
      final String uuid = uuidProvider.call();
      if (uuid != null) {
        builder.addHeader(HEADER_UUID, uuid);
      }
    }
    return chain.proceed(builder.build());
  }
}
