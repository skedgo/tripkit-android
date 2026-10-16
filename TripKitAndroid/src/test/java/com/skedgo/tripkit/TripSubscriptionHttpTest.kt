package com.skedgo.tripkit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import io.mockk.mockk
import io.reactivex.Observable
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class TripSubscriptionHttpTest {
    private lateinit var server: MockWebServer
    private lateinit var updater: TripUpdaterImpl

    @Before fun setup() {
        server = MockWebServer().apply { start() }
        val api = Retrofit.Builder().baseUrl(server.url("/"))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(TripUpdateApi::class.java)
        updater = TripUpdaterImpl(mockk(), api, Gson())
    }

    @After fun cleanup() { server.shutdown() }

    private fun subscribe() = Observable.just(server.url("/subscribe").toString())
        .flatMap { updater.tripSubscription(it).firstOrError().toObservable() }
        .test().awaitDone(3, TimeUnit.SECONDS)

    @Test fun `HTTP 204 acknowledges subscription without a response body`() {
        server.enqueue(MockResponse().setResponseCode(204))
        subscribe().assertValueCount(1).assertComplete().assertNoErrors()
    }

    @Test fun `HTTP 200 preserves successful subscription acknowledgement`() {
        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))
        subscribe().assertValueCount(1).assertComplete().assertNoErrors()
    }

    @Test fun `HTTP 403 remains a failed subscription`() {
        server.enqueue(MockResponse().setResponseCode(403).setBody("{}"))
        subscribe().assertNoValues().assertError { it is HttpException && it.code() == 403 }
    }
}
