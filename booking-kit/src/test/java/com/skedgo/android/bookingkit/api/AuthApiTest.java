package com.skedgo.android.bookingkit.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.android.bookingkit.BuildConfig;
import com.skedgo.android.bookingkit.model.AuthProvider;
import com.skedgo.android.bookingkit.model.GsonAdaptersAuthProvider;
import com.skedgo.android.bookingkit.model.ImmutableAuthProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.observers.TestSubscriber;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AuthApiTest {
  private MockWebServer server;
  private AuthApi api;
  private HttpUrl baseUrl;

  @Before public void before() {
    final Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new GsonAdaptersAuthProvider())
        .create();
    server = new MockWebServer();
    baseUrl = server.url("/");
    api = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(AuthApi.class);
  }

  @After public void after() throws IOException {
    server.shutdown();
  }

  @Test public void fetchProvidersSuccessfully() {
    final MockResponse mockResponse = new MockResponse();
    mockResponse.setResponseCode(200);
    mockResponse.setBody(
        "[\n" +
            "  {\n" +
            "    \"modeIdentifier\": \"ps_tnc_LYFT\",\n" +
            "    \"provider\": \"lyft\",\n" +
            "    \"action\": \"signin\",\n" +
            "    \"url\": \"https://granduni.buzzhives.com/satapp-beta/auth/lyft/signin\",\n" +
            "    \"actionTitle\": \"Connect\",\n" +
            "    \"status\": \"Account not yet connected\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"modeIdentifier\": \"ps_tnc_UBER\",\n" +
            "    \"provider\": \"uber\",\n" +
            "    \"action\": \"signin\",\n" +
            "    \"url\": \"https://granduni.buzzhives.com/satapp-beta/auth/uber/signin\",\n" +
            "    \"actionTitle\": \"Connect\",\n" +
            "    \"status\": \"Account not yet connected\"\n" +
            "  }\n" +
            "]"
    );
    server.enqueue(mockResponse);

    final HttpUrl url = baseUrl.newBuilder()
        .addPathSegment("auth")
        .addPathSegment("US_CO_Denver")
        .build();
    final TestSubscriber<List<AuthProvider>> subscriber = new TestSubscriber<>();
    api.fetchProvidersAsync(url).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue(Arrays.<AuthProvider>asList(
        ImmutableAuthProvider.builder()
            .modeIdentifier("ps_tnc_LYFT")
            .provider("lyft")
            .action("signin")
            .url("https://granduni.buzzhives.com/satapp-beta/auth/lyft/signin")
            .actionTitle("Connect")
            .status("Account not yet connected")
            .build(),
        ImmutableAuthProvider.builder()
            .modeIdentifier("ps_tnc_UBER")
            .provider("uber")
            .action("signin")
            .url("https://granduni.buzzhives.com/satapp-beta/auth/uber/signin")
            .actionTitle("Connect")
            .status("Account not yet connected")
            .build()
    ));
  }
}