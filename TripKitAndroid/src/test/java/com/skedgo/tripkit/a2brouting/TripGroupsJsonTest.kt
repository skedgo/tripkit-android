package com.skedgo.tripkit.a2brouting

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.skedgo.tripkit.TripKitAndroidRobolectricTest
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import thuytrinh.mockwebserverrule.MockWebServerRule
import com.skedgo.tripkit.routing.Trip
import com.skedgo.tripkit.routing.TripGroup
import java.io.IOException
import java.lang.reflect.Type
import java.util.Collections
import java.util.HashMap
import kotlin.system.measureTimeMillis
import org.assertj.core.api.Assertions.assertThat

@RunWith(AndroidJUnit4::class)
class TripGroupsJsonTest : TripKitAndroidRobolectricTest() {
    private val TRIP_GROUP_TYPE: Type = object : TypeToken<List<TripGroup>>() {}.type

    @Rule
    @JvmField
    val serverRule = MockWebServerRule()

    private lateinit var api: FailoverA2bRoutingApi

    @Before
    fun before() {
        val a2bRoutingApi = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(serverRule.server.url("/"))
            .build()
            .create(A2bRoutingApi::class.java)

        api = FailoverA2bRoutingApi(
            ApplicationProvider.getApplicationContext<Context>().resources,
            Gson(),
            a2bRoutingApi
        )
    }

    @Test
    @Throws(IOException::class)
    fun shouldNullifyTripRawSegmentList() {
        val mockResponse: MockResponse = MockWebServerRule.createMockResponse("/large-routing.json")
        serverRule.server.enqueue(mockResponse)

        val subscriber: TestObserver<List<TripGroup>> = api.fetchRoutesAsync(
            listOf(serverRule.server.url("/").toString()),
            emptyList(),
            emptyList(),
            emptyList(),
            HashMap()
        ).test()

        val tripGroups: List<TripGroup> = subscriber.values()[0]
        assertThat(tripGroups).isNotNull.isNotEmpty

        // Should nullify `rawSegmentList`.
        // Otherwise, gson will convert it to json and result in
        // a very large json which is unnecessary.
        tripGroups.forEach { tripGroup ->
            val trips: List<Trip>? = tripGroup.trips
            trips?.forEach { trip ->
                assertThat(trip.rawSegmentList).isNull()
            }
        }

        // Call for sake of benchmark.
        toJson(Gson(), tripGroups)
    }

    /**
     * We may need to put a breakpoint to check whether
     * the result `json` includes `rawSegmentList` or not.
     */
    private fun toJson(gson: Gson, groups: List<TripGroup>) {
        val time = measureTimeMillis {
            val json = gson.toJson(groups, TRIP_GROUP_TYPE)
            println("Generated JSON: $json")
        }
        println("Time: ${time}ms")
    }
}
