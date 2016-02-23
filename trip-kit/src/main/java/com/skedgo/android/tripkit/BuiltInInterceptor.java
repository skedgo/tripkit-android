package com.skedgo.android.tripkit;

import org.immutables.value.Value;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Response;

import static org.immutables.value.Value.Style.BuilderVisibility.PACKAGE;
import static org.immutables.value.Value.Style.ImplementationVisibility.PRIVATE;

@Value.Immutable
@Value.Style(visibility = PRIVATE, builderVisibility = PACKAGE)
public abstract class BuiltInInterceptor implements Interceptor {
  private static final String HEADER_APP_VERSION = "X-TripGo-Version";
  private static final String HEADER_REGION_ELIGIBILITY = "X-TripGo-RegionEligibility";
  private static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";

  public static Builder builder() {
    return new BuiltInInterceptorBuilder();
  }

  public abstract String appVersion();
  public abstract String regionEligibility();
  public abstract Locale locale();

  @Override public Response intercept(Chain chain) throws IOException {
    return chain.proceed(
        chain.request()
            .newBuilder()
            .addHeader(HEADER_ACCEPT_LANGUAGE, locale().getLanguage())
            .addHeader(HEADER_APP_VERSION, appVersion())
            .addHeader(HEADER_REGION_ELIGIBILITY, regionEligibility())
            .build());
  }

  public interface Builder {
    Builder appVersion(String appVersion);
    Builder regionEligibility(String regionEligibility);
    Builder locale(Locale locale);
    BuiltInInterceptor build();
  }
}