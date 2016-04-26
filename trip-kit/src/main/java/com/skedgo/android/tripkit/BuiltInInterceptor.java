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

@Value.Style(newBuilder = "create")
public final class BuiltInInterceptor implements Interceptor {
  static final String HEADER_APP_VERSION = "X-TripGo-Version";
  static final String HEADER_REGION_ELIGIBILITY = "X-TripGo-RegionEligibility";
  static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
  static final String HEADER_USER_TOKEN = "userToken";

  public final String appVersion;
  public final String regionEligibility;
  public final Locale locale;
  @Nullable public final Func0<String> userTokenProvider;

  private BuiltInInterceptor(
      String appVersion,
      String regionEligibility,
      Locale locale,
      @Nullable Func0<String> userTokenProvider) {
    this.appVersion = appVersion;
    this.regionEligibility = regionEligibility;
    this.locale = locale;
    this.userTokenProvider = userTokenProvider;
  }

  @Builder.Factory
  static BuiltInInterceptor builtInInterceptor(
      String appVersion,
      String regionEligibility,
      Locale locale,
      @Nullable Func0<String> userTokenProvider) {
    return new BuiltInInterceptor(
        appVersion,
        regionEligibility,
        locale,
        userTokenProvider
    );
  }

  @Override public Response intercept(Chain chain) throws IOException {
    final Request.Builder builder = chain.request().newBuilder()
        .addHeader(HEADER_ACCEPT_LANGUAGE, locale.getLanguage())
        .addHeader(HEADER_APP_VERSION, appVersion)
        .addHeader(HEADER_REGION_ELIGIBILITY, regionEligibility);
    if (userTokenProvider != null) {
      builder.addHeader(HEADER_USER_TOKEN, userTokenProvider.call());
    }

    return chain.proceed(builder.build());
  }
}