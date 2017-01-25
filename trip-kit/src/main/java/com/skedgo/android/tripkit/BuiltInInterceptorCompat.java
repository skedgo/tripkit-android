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

/**
 * We'll migrate to {@link okhttp3.OkHttpClient} soon.
 */
@Deprecated
@Value.Style(newBuilder = "create")
public final class BuiltInInterceptorCompat implements Interceptor {
  private static final String HEADER_APP_VERSION = "X-TripGo-Version";
  private static final String HEADER_REGION_ELIGIBILITY = "X-TripGo-RegionEligibility";
  private static final String HEADER_UUID = "X-TripGo-UUID";
  private static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
  private static final String HEADER_USER_TOKEN = "userToken";

  private final String appVersion;
  private final String regionEligibility;
  private final Locale locale;
  @Nullable private final Func0<String> uuidProvider;
  @Nullable private final Func0<String> userTokenProvider;

  private BuiltInInterceptorCompat(
      String appVersion,
      String regionEligibility,
      Locale locale,
      @Nullable Func0<String> uuidProvider,
      @Nullable Func0<String> userTokenProvider) {
    this.appVersion = appVersion;
    this.regionEligibility = regionEligibility;
    this.locale = locale;
    this.uuidProvider = uuidProvider;
    this.userTokenProvider = userTokenProvider;
  }

  @Builder.Factory
  static BuiltInInterceptorCompat builtInInterceptorCompat(
      String appVersion,
      String regionEligibility,
      Locale locale,
      @Nullable Func0<String> uuidProvider,
      @Nullable Func0<String> userTokenProvider) {
    return new BuiltInInterceptorCompat(
        appVersion,
        regionEligibility,
        locale,
        uuidProvider,
        userTokenProvider
    );
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    final Request.Builder builder = chain.request().newBuilder()
        .addHeader(HEADER_ACCEPT_LANGUAGE, locale.getLanguage())
        .addHeader(HEADER_APP_VERSION, appVersion)
        .addHeader(HEADER_REGION_ELIGIBILITY, regionEligibility);
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