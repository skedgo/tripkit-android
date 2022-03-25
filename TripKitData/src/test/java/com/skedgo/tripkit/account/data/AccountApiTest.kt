package com.skedgo.tripkit.account.data

import org.junit.After
import org.junit.Test

import java.io.IOException

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.HttpException
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import org.assertj.core.api.Java6Assertions.assertThat

class AccountApiTest {
  private val server: MockWebServer = MockWebServer()
  private val api: AccountApi by lazy {
     Retrofit.Builder()
      .baseUrl(server.url("/"))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(AccountApi::class.java)
  }

  @After
  @Throws(IOException::class)
  fun after() {
    server.shutdown()
  }

  @Test
  @Throws(IOException::class, InterruptedException::class)
  fun signUpSuccessfully() {
    val response = MockResponse()
        .setBody("{\"changed\":true,\"userToken\":\"id5E2lbNJ37V1HwAUKmpLaPSSmpzHK\"}")
    server.enqueue(response)

    val subscriber = api.signUp(
        ImmutableSignUpBody.builder()
            .password("Some password")
            .username("Some username")
            .build()
    ).test()

    subscriber.awaitTerminalEvent()
    subscriber.assertNoErrors()
    subscriber.assertValue(
        ImmutableSignUpResponse.builder()
            .userToken("id5E2lbNJ37V1HwAUKmpLaPSSmpzHK")
            .build()
    )

    val request = server.takeRequest()
    assertThat(request.path).containsOnlyOnce("/account/signup")
  }

  @Test
  @Throws(IOException::class, InterruptedException::class)
  fun logInSuccessfully() {
    val mockResponse = MockResponse()
        .setResponseCode(200)
        .setBody("{\"changed\":true,\"userToken\":\"6XzsKaatH0rZNkbDieRligNLy3iYjn\"}")
    server.enqueue(mockResponse)

    val subscriber =  api.logIn(
        ImmutableLogInBody.builder()
            .username("Some username")
            .password("Some password")
            .build()
    ).test()

    subscriber.awaitTerminalEvent()
    subscriber.assertNoErrors()
    subscriber.assertValue(
        ImmutableLogInResponse.builder()
            .userToken("6XzsKaatH0rZNkbDieRligNLy3iYjn")
            .changed(true)
            .build()
    )

    val request = server.takeRequest()
    assertThat(request.path).containsOnlyOnce("/account/login")
  }

  @Test
  fun failToLogIn() {
    val mockResponse = MockResponse()
        .setResponseCode(401)
        .setBody("{\"error\":\"The user or password you entered is incorrect.\",\"errorCode\":401,\"usererror\":true}")
    server.enqueue(mockResponse)

    val subscriber = api.logIn(
        ImmutableLogInBody.builder()
            .username("Some username")
            .password("Some password")
            .build()
    ).test()

    subscriber.awaitTerminalEvent()
    subscriber.assertError(HttpException::class.java)
  }

  @Test
  @Throws(InterruptedException::class)
  fun logOutSuccessfully() {
    val mockResponse = MockResponse()
        .setResponseCode(200)
        .setBody("{\"changed\":true}")
    server.enqueue(mockResponse)

    val subscriber = api.logOut().test()

    subscriber.awaitTerminalEvent()
    subscriber.assertNoErrors()
    subscriber.assertValue(ImmutableLogOutResponse.builder().build())

    val request = server.takeRequest()
    assertThat(request.path).containsOnlyOnce("/account/logout")
  }
}
