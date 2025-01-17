package com.skedgo.tripkit.a2brouting

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.skedgo.tripkit.TripKitAndroidRobolectricTest
import com.skedgo.tripkit.extensions.fromJson
import com.skedgo.tripkit.routing.Trip
import com.skedgo.tripkit.routing.TripGroup
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import thuytrinh.mockwebserverrule.MockWebServerRule
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

@RunWith(AndroidJUnit4::class)
class TripGroupsJsonTest : TripKitAndroidRobolectricTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val immediateScheduler: Scheduler = object : Scheduler() {

        override fun createWorker() = ExecutorScheduler.ExecutorWorker { it.run() }

        // This prevents errors when scheduling a delay
        override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
            return super.scheduleDirect(run, 0, unit)
        }

    }

    fun initRx() {
        RxJavaPlugins.setIoSchedulerHandler { immediateScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediateScheduler }
        RxAndroidPlugins.setMainThreadSchedulerHandler { immediateScheduler }
    }

    fun tearDownRx() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    private val TRIP_GROUP_TYPE: Type = object : TypeToken<List<TripGroup>>() {}.type

    @Rule
    @JvmField
    val serverRule = MockWebServerRule()

    private lateinit var api: FailoverA2bRoutingApi

    @Before
    fun before() {
        initRx()
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

    @After
    fun after() {
        tearDownRx()
    }

    @Test
    @Throws(IOException::class)
    fun rawSegmentListShouldNotBeEmpty() {
        val filePath =
            Paths.get("src", "test", "resources", "trip-groups.json").toAbsolutePath().toString()

        val jsonResponse = File(filePath).readText()

        // Create a mock of the API service
        val apiMock = mockk<FailoverA2bRoutingApi>(relaxed = true)

        // Mock the response of the API call using MockK
        val path = "mocked_url"
        every {
            apiMock.fetchRoutesAsync(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Observable.just(
            Gson().fromJson(jsonResponse)
        )

        // Call the API method in the test
        val subscriber: TestObserver<List<TripGroup>> = apiMock.fetchRoutesAsync(
            listOf(path),
            emptyList(),
            emptyList(),
            emptyList(),
            HashMap()
        ).test()

        // Extract the response from the subscriber
        val tripGroups: List<TripGroup> = subscriber.values()[0]
        assertThat(tripGroups).isNotNull.isNotEmpty

        // Verify that `rawSegmentList` is not null
        tripGroups.forEach { tripGroup ->
            val trips: List<Trip>? = tripGroup.trips
            trips?.forEach { trip ->
                assertThat(trip.rawSegmentList).isNotNull.isNotEmpty
            }
        }

        // Call toJson for benchmark purposes
        toJson(Gson(), tripGroups)

        // Verify that the API call was made with the correct parameters
        verify { apiMock.fetchRoutesAsync(any(), any(), any(), any(), any()) }
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
