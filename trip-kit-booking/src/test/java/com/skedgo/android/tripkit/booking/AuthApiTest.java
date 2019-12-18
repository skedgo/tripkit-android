package com.skedgo.android.tripkit.booking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.observers.TestObserver;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class AuthApiTest {
  private MockWebServer server;
  private AuthApi api;
  private HttpUrl baseUrl;

  @Before public void before() {
    final Gson gson = new GsonBuilder()
        .registerTypeAdapter(FormField.class, new FormFieldJsonAdapter())
        .create();
    server = new MockWebServer();
    baseUrl = server.url("/");
    api = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(AuthApi.class);
  }

  @After public void after() throws IOException {
    server.shutdown();
  }

  @Test public void fetchProvidersSuccessfully() throws IOException {
    final MockResponse mockResponse = new MockResponse();
    mockResponse.setResponseCode(200);
    mockResponse.setBody(IOUtils.toString(
        getClass().getResourceAsStream("/auth-US_CO_Denver.json"),
        Charset.defaultCharset()
    ));
    server.enqueue(mockResponse);

    final HttpUrl url = baseUrl.newBuilder()
        .addPathSegment("auth")
        .addPathSegment("US_CO_Denver")
        .build();
    final TestObserver<List<AuthProvider>> subscriber = api.fetchProvidersAsync(url).test();

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

  @Test public void signInSuccessfully() throws IOException {
    final MockResponse mockResponse = new MockResponse()
        .setResponseCode(200)
        .setBody(IOUtils.toString(
            getClass().getResourceAsStream("/auth-uber-signin.json"),
            Charset.defaultCharset()
        ));
    server.enqueue(mockResponse);

    final String url = baseUrl.newBuilder()
        .addPathSegment("auth")
        .addPathSegment("uber")
        .addPathSegment("signin")
        .build()
        .toString();
    final TestObserver<BookingForm> subscriber = api.signInAsync(url).test();

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    final BookingForm form = subscriber.values().get(0);
    assertThat(form.getTitle()).isEqualTo("Authorization");
    assertThat(form.getAction()).isNotNull();
    assertThat(form.getForm()).hasSize(1);
  }

  @Test public void logOutSuccessfully() {
    final MockResponse mockResponse = new MockResponse()
        .setResponseCode(200)
        .setBody("{\"changed\":true}");
    server.enqueue(mockResponse);

    final String url = baseUrl.newBuilder()
        .addPathSegments("auth/uber/logout")
        .build()
        .toString();
    final TestObserver<LogOutResponse> subscriber = api.logOutAsync(url).test();

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue(
        ImmutableLogOutResponse.builder()
            .changed(true)
            .build()
    );
  }
}