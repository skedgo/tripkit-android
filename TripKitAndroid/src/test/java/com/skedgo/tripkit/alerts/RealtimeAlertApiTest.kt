package com.skedgo.tripkit.alerts

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.booking.ui.base.MockKTest
import com.skedgo.tripkit.common.model.realtimealert.ImmutableRealtimeAlert
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import thuytrinh.mockwebserverrule.MockWebServerRule.createMockResponse

import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RealtimeAlertApiTest: MockKTest() {

    @get:Rule
    val server = MockWebServer()
    private lateinit var api: RealtimeAlertApi

    @Before
    fun setUp() {
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.trampoline()))
            .build()
            .create(RealtimeAlertApi::class.java)
    }

    @Test
    @Throws(IOException::class)
    fun `fetch realtime alerts successfully`() {
        // Arrange
        server.enqueue(createMockResponse("/alerts-transit.json"))
        val regionName = "AU_NSW_Sydney"

        // Act
        val testObserver: TestObserver<RealtimeAlertResponse> = api.fetchRealtimeAlertsAsync(
            server.url("/").toString(),
            regionName
        ).test()

        // Assert
        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()

        val expectedResponse = ImmutableRealtimeAlertResponse.builder()
            .alerts(
                listOf(
                    ImmutableAlertBlock.builder()
                        .alert(
                            ImmutableRealtimeAlert.builder()
                                .remoteHashCode(707713596)
                                .severity("warning")
                                .title("Trackwork - Blue Mountains Line")
                                .text("Monday 29 August to Friday 2 September \n\n- Buses replace trains between Mount Victoria and Lithgow.\n- Trains to and from Bathurst run to a changed timetable.")
                                .url("http://www.sydneytrains.info/service_updates/service_interruptions/")
                                .build()
                        ).operators("operator1", "operator2")
                        .stopCodes("stopCode1", "stopCode2")
                        .serviceTripIDs("test1", "test2")
                        .routes(
                            ImmutableRoute.builder()
                                .id("test")
                                .build()
                        )
                        .build(),
                    ImmutableAlertBlock.builder()
                        .alert(
                            ImmutableRealtimeAlert.builder()
                                .remoteHashCode(697250695)
                                .severity("warning")
                                .title("Wharf Closed")
                                .text("McMahons Point Wharf Closed. Wharf closed for planned upgrade.")
                                .url("")
                                .build()
                        ).operators("operator1", "operator2")
                        .stopCodes("stopCode1", "stopCode2")
                        .serviceTripIDs("test1", "test2")
                        .routes(
                            ImmutableRoute.builder()
                                .id("test")
                                .build()
                        )
                        .build()
                )
            )
            .build()

        testObserver.assertValue(expectedResponse)
    }
}