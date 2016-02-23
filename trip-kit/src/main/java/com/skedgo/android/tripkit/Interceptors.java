package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Locale;

public final class Interceptors {
  private Interceptors() {}

  public static Interceptor addCustomRequestHeaders(
      @NonNull String appVersion,
      @NonNull String regionEligibility,
      @NonNull Locale locale) {
    return new CustomRequestHeaderInterceptor(appVersion, regionEligibility, locale);
  }

  private static final class CustomRequestHeaderInterceptor implements Interceptor {
    private static final String HEADER_APP_VERSION = "X-TripGo-Version";
    private static final String HEADER_REGION_ELIGIBILITY = "X-TripGo-RegionEligibility";
    private static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";

    private final String appVersion;
    private final String regionEligibility;
    private final Locale locale;

    private CustomRequestHeaderInterceptor(
        @NonNull String appVersion,
        @NonNull String regionEligibility,
        @NonNull Locale locale) {
      this.appVersion = appVersion;
      this.regionEligibility = regionEligibility;
      this.locale = locale;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
      return chain.proceed(
          chain.request()
              .newBuilder()
              .addHeader(HEADER_ACCEPT_LANGUAGE, locale.getLanguage())
              .addHeader(HEADER_APP_VERSION, appVersion)
              .addHeader(HEADER_REGION_ELIGIBILITY, regionEligibility)
              .build());
    }
  }
}