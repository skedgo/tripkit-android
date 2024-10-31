package com.skedgo.tripkit.booking

import com.google.gson.GsonBuilder
import io.reactivex.observers.TestObserver
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.apache.commons.io.IOUtils
import org.assertj.core.api.Java6Assertions
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.nio.charset.Charset
import java.util.Arrays

@RunWith(RobolectricTestRunner::class)
class AuthApiTest {
    private lateinit var server: MockWebServer
    private lateinit var api: AuthApi
    private lateinit var baseUrl: HttpUrl

    @Before
    fun before() {
        val gson = GsonBuilder()
            .registerTypeAdapter(FormField::class.java, FormFieldJsonAdapter())
            .create()
        server = MockWebServer()
        baseUrl = server.url("/")
        api = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(AuthApi::class.java)
    }

    @After
    fun after() {
        server.shutdown()
    }

    @Test
    fun fetchProvidersSuccessfully() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                this::class.java.getResourceAsStream("/auth-US_CO_Denver.json")
                    ?.bufferedReader(Charset.defaultCharset())?.readText() ?: ""
            )
        server.enqueue(mockResponse)

        val url = baseUrl.newBuilder()
            .addPathSegment("auth")
            .addPathSegment("US_CO_Denver")
            .build()
        val subscriber: TestObserver<List<AuthProvider>> = api.fetchProvidersAsync(url).test()

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertValue(
            listOf(
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
            )
        )
    }

    @Test
    fun signInSuccessfully() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                this::class.java.getResourceAsStream("/auth-uber-signin.json")
                    ?.bufferedReader(Charset.defaultCharset())?.readText() ?: ""
            )
        server.enqueue(mockResponse)

        val url = baseUrl.newBuilder()
            .addPathSegment("auth")
            .addPathSegment("uber")
            .addPathSegment("signin")
            .build().toString()
        val subscriber: TestObserver<BookingForm> = api.signInAsync(url).test()

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        val form = subscriber.values()[0]
        assertEquals("Authorization", form.title)
        assert(form.action != null)
        assertEquals(1, form.form.size)
    }

    @Test
    fun logOutSuccessfully() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{\"changed\":true}")
        server.enqueue(mockResponse)

        val url = baseUrl.newBuilder()
            .addPathSegments("auth/uber/logout")
            .build().toString()
        val subscriber: TestObserver<LogOutResponse> = api.logOutAsync(url).test()

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertValue(
            ImmutableLogOutResponse.builder()
                .changed(true)
                .build()
        )
    }
}