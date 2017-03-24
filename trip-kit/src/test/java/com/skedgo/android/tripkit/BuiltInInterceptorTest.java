package com.skedgo.android.tripkit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import rx.functions.Func0;
import thuytrinh.mockwebserverrule.MockWebServerRule;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class BuiltInInterceptorTest {
  @Rule public MockWebServerRule serverRule = new MockWebServerRule();
  private HttpUrl url;

  @Before public void before() {
    url = serverRule.server.url("/");
  }

  @Test public void attachHeaders_full() throws IOException, InterruptedException {
    final Interceptor interceptor = BuiltInInterceptorBuilder.create()
        .appVersion("Some version")
        .locale(Locale.JAPANESE)
        .getRegionEligibility(new Func0<String>() {
          @Override public String call() {
            return "Some id";
          }
        })
        .userTokenProvider(new Func0<String>() {
          @Override public String call() {
            return "Some token";
          }
        })
        .uuidProvider(new Func0<String>() {
          @Override public String call() {
            return "Some UUID";
          }
        })
        .build();

    serverRule.server.enqueue(new MockResponse());
    executeSampleRequest(interceptor);

    final RecordedRequest recordedRequest = serverRule.server.takeRequest();
    assertThat(recordedRequest.getHeader("X-TripGo-Version")).isEqualTo("Some version");
    assertThat(recordedRequest.getHeader("Accept-Language")).isEqualTo(Locale.JAPANESE.getLanguage());
    assertThat(recordedRequest.getHeader("X-TripGo-RegionEligibility")).isEqualTo("Some id");
    assertThat(recordedRequest.getHeader("userToken")).isEqualTo("Some token");
    assertThat(recordedRequest.getHeader("X-TripGo-UUID")).isEqualTo("Some UUID");
  }

  @Test public void attachHeaders_noUuid_withoutProvider()
      throws IOException, InterruptedException {
    final Interceptor interceptor = BuiltInInterceptorBuilder.create()
        .appVersion("Some version")
        .locale(Locale.JAPANESE)
        .getRegionEligibility(new Func0<String>() {
          @Override public String call() {
            return "Some id";
          }
        })
        .build();

    serverRule.server.enqueue(new MockResponse());
    executeSampleRequest(interceptor);

    final RecordedRequest recordedRequest = serverRule.server.takeRequest();
    assertThat(recordedRequest.getHeader("X-TripGo-UUID")).isNull();
  }

  @Test public void attachHeaders_noUuid_withProvider()
      throws IOException, InterruptedException {
    final Interceptor interceptor = BuiltInInterceptorBuilder.create()
        .appVersion("Some version")
        .locale(Locale.JAPANESE)
        .getRegionEligibility(new Func0<String>() {
          @Override public String call() {
            return "Some id";
          }
        })
        .uuidProvider(new Func0<String>() {
          @Override public String call() {
            return null;
          }
        })
        .build();

    serverRule.server.enqueue(new MockResponse());
    executeSampleRequest(interceptor);

    final RecordedRequest recordedRequest = serverRule.server.takeRequest();
    assertThat(recordedRequest.getHeader("X-TripGo-UUID")).isNull();
  }

  @Test(expected = IllegalStateException.class)
  public void appVersionIsMandatory() {
    BuiltInInterceptorBuilder.create()
        .getRegionEligibility(new Func0<String>() {
          @Override public String call() {
            return "Some name";
          }
        })
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
        .getRegionEligibility(new Func0<String>() {
          @Override public String call() {
            return "Some name";
          }
        })
        .build();
  }

  @Test public void userTokenProviderIsOptional() {
    BuiltInInterceptorBuilder.create()
        .appVersion("Some version")
        .locale(Locale.JAPANESE)
        .getRegionEligibility(new Func0<String>() {
          @Override public String call() {
            return "Some name";
          }
        })
        .build();
  }

  private void executeSampleRequest(Interceptor interceptor) throws IOException {
    final OkHttpClient httpClient = new OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build();
    final Request request = new Request.Builder().url(url).build();
    httpClient.newCall(request).execute();
  }
}
