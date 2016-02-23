package com.skedgo.android.tripkit;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class InterceptorsTest {
  private OkHttpClient httpClient;

  @Before
  public void setUp() {
    httpClient = new OkHttpClient();
  }

  @Test
  public void shouldAddCustomHeadersIntoRequest() throws Exception {
    final String expectedAppVersion = "a4.3";
    final String expectedRegionEligibility = "beta";

    httpClient.interceptors().add(Interceptors.addCustomRequestHeaders(
        expectedAppVersion,
        expectedRegionEligibility,
        Locale.FRANCE
    ));

    final AtomicReference<Headers> headersHolder = new AtomicReference<>();
    httpClient.interceptors().add(new Interceptor() {
      @Override
      public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();
        headersHolder.set(request.headers());
        return chain.proceed(request);
      }
    });

    httpClient.newCall(new Request.Builder()
                           .url("https://skedgo.com/tripgo")
                           .build())
        .execute();

    final Headers headers = headersHolder.get();
    assertThat(headers)
        .describedAs("Should catch headers")
        .isNotNull();

    final String actualAppVersion = headers.get("X-TripGo-Version");
    assertThat(actualAppVersion)
        .describedAs("Should add app version '%s' to headers", expectedAppVersion)
        .isEqualTo(expectedAppVersion);

    final String actualRegionEligibility = headers.get("X-TripGo-RegionEligibility");
    assertThat(actualAppVersion)
        .describedAs("Should add region eligibility '%s' to headers", actualRegionEligibility)
        .isEqualTo(expectedAppVersion);

    final String actualLanguage = headers.get("Accept-Language");
    assertThat(actualLanguage)
        .describedAs("Should add locale to headers")
        .isEqualTo(Locale.FRANCE.getLanguage());
  }
}