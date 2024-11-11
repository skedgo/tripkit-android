package com.skedgo.tripkit.tsp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.TripKitAndroidRobolectricTest
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Java6Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import thuytrinh.mockwebserverrule.MockWebServerRule
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RegionInfoApiTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var api: RegionInfoApi
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.trampoline()))
            .build()
            .create(RegionInfoApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    @Throws(IOException::class)
    fun `successfully fetch region info`() {
        val mockResponse = MockResponse()
            .setBody("{ \"regions\": [{ \"transitWheelchairAccessibility\": true }] }")
        mockWebServer.enqueue(mockResponse)

        val testObserver = TestObserver<RegionInfoResponse>()
        val regionInfoBody = ImmutableRegionInfoBody.of("AU_NSW_Sydney")

        api.fetchRegionInfoAsync("/regionInfo.json", regionInfoBody)
            .subscribe(testObserver)

        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()

        val response = testObserver.values()[0]
        assert(response.regions().size == 1)
        val regionInfo = response.regions()[0]
        assert(regionInfo.transitWheelchairAccessibility())
    }
}