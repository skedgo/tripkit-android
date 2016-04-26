package com.skedgo.android.tripkit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import rx.functions.Func0;

import static com.skedgo.android.tripkit.BuiltInInterceptor.HEADER_ACCEPT_LANGUAGE;
import static com.skedgo.android.tripkit.BuiltInInterceptor.HEADER_APP_VERSION;
import static com.skedgo.android.tripkit.BuiltInInterceptor.HEADER_REGION_ELIGIBILITY;
import static com.skedgo.android.tripkit.BuiltInInterceptor.HEADER_USER_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class BuiltInInterceptorTest {
  @Test public void attachHeaders() throws IOException, InterruptedException {
    final MockWebServer server = new MockWebServer();
    server.enqueue(new MockResponse());
    server.start();

    final Interceptor interceptor = BuiltInInterceptorBuilder.create()
        .appVersion("Some version")
        .locale(Locale.JAPANESE)
        .regionEligibility("Some id")
        .userTokenProvider(new Func0<String>() {
          @Override public String call() {
            return "Some token";
          }
        })
        .build();
    final OkHttpClient httpClient = new OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build();
    final Request request = new Request.Builder()
        .url(server.url("/"))
        .build();
    httpClient.newCall(request).execute();

    final RecordedRequest recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getHeader(HEADER_APP_VERSION)).isEqualTo("Some version");
    assertThat(recordedRequest.getHeader(HEADER_ACCEPT_LANGUAGE)).isEqualTo(Locale.JAPANESE.getLanguage());
    assertThat(recordedRequest.getHeader(HEADER_REGION_ELIGIBILITY)).isEqualTo("Some id");
    assertThat(recordedRequest.getHeader(HEADER_USER_TOKEN)).isEqualTo("Some token");

    server.shutdown();
  }

  @Test(expected = IllegalStateException.class)
  public void appVersionIsMandatory() {
    BuiltInInterceptorBuilder.create()
        .regionEligibility("Some name")
        .locale(Locale.US)
        .build();
  }

  @Test(expected = IllegalStateException.class)
  public void regionEligibilityIsMandatory() {
    BuiltInInterceptorBuilder.create()
        .appVersion("Some version")
        .locale(Locale.US)
        .build();
  }

  @Test(expected = IllegalStateException.class)
  public void localeIsMandatory() {
    BuiltInInterceptorBuilder.create()
        .appVersion("Some version")
        .regionEligibility("Some name")
        .build();
  }

  @Test public void userTokenProviderIsOptional() {
    BuiltInInterceptorBuilder.create()
        .appVersion("Some version")
        .locale(Locale.JAPANESE)
        .regionEligibility("Some name")
        .build();
  }
}