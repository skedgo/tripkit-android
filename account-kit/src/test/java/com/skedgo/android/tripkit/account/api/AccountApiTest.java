package com.skedgo.android.tripkit.account.api;

import com.skedgo.android.tripkit.account.BuildConfig;
import com.skedgo.android.tripkit.account.model.ImmutableLogInBody;
import com.skedgo.android.tripkit.account.model.ImmutableLogInResponse;
import com.skedgo.android.tripkit.account.model.ImmutableLogOutResponse;
import com.skedgo.android.tripkit.account.model.ImmutableSignUpBody;
import com.skedgo.android.tripkit.account.model.ImmutableSignUpResponse;
import com.skedgo.android.tripkit.account.model.LogInResponse;
import com.skedgo.android.tripkit.account.model.LogOutResponse;
import com.skedgo.android.tripkit.account.model.SignUpResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AccountApiTest {
  private MockWebServer server;
  private AccountApi api;

  @Before public void before() {
    server = new MockWebServer();
    api = new Retrofit.Builder()
        .baseUrl(server.url("/"))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AccountApi.class);
  }

  @Test public void signUpSuccessfully() throws IOException, InterruptedException {
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

    final RecordedRequest request = server.takeRequest();
    assertThat(request.getPath()).containsOnlyOnce("/account/signup");
  }

  @Test public void logInSuccessfully() throws IOException, InterruptedException {
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

    final RecordedRequest request = server.takeRequest();
    assertThat(request.getPath()).containsOnlyOnce("/account/login");
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

  @Test public void logOutSuccessfully() throws InterruptedException {
    final MockResponse mockResponse = new MockResponse()
        .setResponseCode(200)
        .setBody("{\"changed\":true}");
    server.enqueue(mockResponse);

    final TestSubscriber<LogOutResponse> subscriber = new TestSubscriber<>();
    api.logOutAsync().subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertValue(
        ImmutableLogOutResponse.builder()
            .changed(true)
            .build()
    );

    final RecordedRequest request = server.takeRequest();
    assertThat(request.getPath()).containsOnlyOnce("/account/logout");
  }

  @After public void after() throws IOException {
    server.shutdown();
  }
}