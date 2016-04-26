package com.skedgo.android.accountkit.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.android.accountkit.BuildConfig;
import com.skedgo.android.accountkit.model.GsonAdaptersLogInBody;
import com.skedgo.android.accountkit.model.GsonAdaptersLogInResponse;
import com.skedgo.android.accountkit.model.GsonAdaptersSignUpBody;
import com.skedgo.android.accountkit.model.GsonAdaptersSignUpResponse;
import com.skedgo.android.accountkit.model.ImmutableLogInBody;
import com.skedgo.android.accountkit.model.ImmutableLogInResponse;
import com.skedgo.android.accountkit.model.ImmutableSignUpBody;
import com.skedgo.android.accountkit.model.ImmutableSignUpResponse;
import com.skedgo.android.accountkit.model.LogInResponse;
import com.skedgo.android.accountkit.model.SignUpResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.observers.TestSubscriber;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AccountApiTest {
  private MockWebServer server;
  private AccountApi api;

  @Before public void before() {
    final Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new GsonAdaptersSignUpBody())
        .registerTypeAdapterFactory(new GsonAdaptersSignUpResponse())
        .registerTypeAdapterFactory(new GsonAdaptersLogInBody())
        .registerTypeAdapterFactory(new GsonAdaptersLogInResponse())
        .create();
    server = new MockWebServer();
    api = new Retrofit.Builder()
        .baseUrl(server.url("/"))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(AccountApi.class);
  }

  @Test public void signUpSuccessfully() throws IOException {
    final MockResponse response = new MockResponse()
        .setBody("{\"changed\":true,\"userToken\":\"id5E2lbNJ37V1HwAUKmpLaPSSmpzHK\"}");
    server.enqueue(response);

    final TestSubscriber<SignUpResponse> subscriber = new TestSubscriber<>();
    api.signUpAsync(
        ImmutableSignUpBody.builder()
            .password("Some password")
            .username("Some username")
            .name("Some name")
            .build()
    ).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue(
        ImmutableSignUpResponse.builder()
            .changed(true)
            .userToken("id5E2lbNJ37V1HwAUKmpLaPSSmpzHK")
            .build()
    );
  }

  @Test public void logInSuccessfully() throws IOException {
    final MockResponse mockResponse = new MockResponse()
        .setResponseCode(200)
        .setBody("{\"changed\":true,\"userToken\":\"6XzsKaatH0rZNkbDieRligNLy3iYjn\"}");
    server.enqueue(mockResponse);

    final TestSubscriber<LogInResponse> subscriber = new TestSubscriber<>();
    api.logInAsync(
        ImmutableLogInBody.builder()
            .username("Some username")
            .password("Some password")
            .build()
    ).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue(
        ImmutableLogInResponse.builder()
            .changed(true)
            .userToken("6XzsKaatH0rZNkbDieRligNLy3iYjn")
            .build()
    );
  }

  @Test public void failToLogIn() {
    final MockResponse mockResponse = new MockResponse()
        .setResponseCode(401)
        .setBody("{\"error\":\"The user or password you entered is incorrect.\",\"errorCode\":401,\"usererror\":true}");
    server.enqueue(mockResponse);

    final TestSubscriber<LogInResponse> subscriber = new TestSubscriber<>();
    api.logInAsync(
        ImmutableLogInBody.builder()
            .username("Some username")
            .password("Some password")
            .build()
    ).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertError(HttpException.class);
  }

  @After public void after() throws IOException {
    server.shutdown();
  }
}