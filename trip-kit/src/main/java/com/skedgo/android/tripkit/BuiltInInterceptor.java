package com.skedgo.android.tripkit;

import org.immutables.builder.Builder;
import org.immutables.value.Value;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Response;

@Value.Style(newBuilder = "create")
public final class BuiltInInterceptor implements Interceptor {
  static final String HEADER_APP_VERSION = "X-TripGo-Version";
  static final String HEADER_REGION_ELIGIBILITY = "X-TripGo-RegionEligibility";
  static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";

  public final String appVersion;
  public final String regionEligibility;
  public final Locale locale;

  private BuiltInInterceptor(
      String appVersion,
      String regionEligibility,
      Locale locale) {
    this.appVersion = appVersion;
    this.regionEligibility = regionEligibility;
    this.locale = locale;
  }

  @Builder.Factory
  static BuiltInInterceptor builtInInterceptor(
      String appVersion,
      String regionEligibility,
      Locale locale) {
    return new BuiltInInterceptor(appVersion, regionEligibility, locale);
  }

  @Override public Response intercept(Chain chain) throws IOException {
    return chain.proceed(
        chain.request()
            .newBuilder()
            .addHeader(HEADER_ACCEPT_LANGUAGE, locale.getLanguage())
            .addHeader(HEADER_APP_VERSION, appVersion)
            .addHeader(HEADER_REGION_ELIGIBILITY, regionEligibility)
            .build());
  }
}