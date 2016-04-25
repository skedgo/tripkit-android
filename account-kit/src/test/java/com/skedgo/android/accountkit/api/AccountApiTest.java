package com.skedgo.android.accountkit.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.android.accountkit.BuildConfig;
import com.skedgo.android.accountkit.model.GsonAdaptersSignUpBody;
import com.skedgo.android.accountkit.model.GsonAdaptersSignUpResponse;
import com.skedgo.android.accountkit.model.ImmutableSignUpBody;
import com.skedgo.android.accountkit.model.ImmutableSignUpResponse;
import com.skedgo.android.accountkit.model.SignUpBody;
import com.skedgo.android.accountkit.model.SignUpResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.observers.TestSubscriber;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AccountApiTest {
  private MockWebServer server;
  private Gson gson;

  @Before public void before() {
    server = new MockWebServer();
    gson = new GsonBuilder()
        .registerTypeAdapterFactory(new GsonAdaptersSignUpBody())
        .registerTypeAdapterFactory(new GsonAdaptersSignUpResponse())
        .create();
  }

  @Test public void test() throws IOException {
    server.enqueue(new MockResponse().setBody("{\"changed\":true,\"userToken\":\"id5E2lbNJ37V1HwAUKmpLaPSSmpzHK\"}"));
    server.start();

    final HttpUrl baseUrl = server.url("/");
    final AccountApi api = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(AccountApi.class);
    final SignUpBody body = ImmutableSignUpBody.builder()
        .password("Some password")
        .username("Some name")
        .build();

    final TestSubscriber<SignUpResponse> subscriber = new TestSubscriber<>();
    api.signUpAsync(body).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    final SignUpResponse expectedResponse = ImmutableSignUpResponse.builder()
        .changed(true)
        .userToken("id5E2lbNJ37V1HwAUKmpLaPSSmpzHK")
        .build();
    subscriber.assertValue(expectedResponse);
  }

  @After public void after() throws IOException {
    server.shutdown();
  }
}