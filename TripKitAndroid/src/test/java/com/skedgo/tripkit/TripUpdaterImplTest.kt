package com.skedgo.tripkit

import android.content.res.Resources
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.routing.RoutingResponse
import com.skedgo.tripkit.routing.Trip
import com.skedgo.tripkit.routing.TripGroup
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class TripUpdaterImplTest {

    private lateinit var resources: Resources
    private lateinit var api: TripUpdateApi
    private lateinit var updater: TripUpdaterImpl

    @Before
    fun before() {
        resources = mockk()
        api = mockk()
        updater = TripUpdaterImpl(resources, api, Gson())
    }

    @Test
    fun shouldReturnEmpty() {
        val trip = mockk<Trip>()
        val updateUrl = "https://goo.gl/f2yv7k"

        every { trip.updateURL } returns updateUrl // Return a proper String value for updateURL

        val response = mockk<RoutingResponse>()
        every { response.tripGroupList } returns null
        every { api.fetchUpdateAsync(updateUrl) } returns Observable.just(response)

        // Mock processRawData as this seems to be part of the issue
        every { response.processRawData(any(), any()) } just Runs

        val subscriber = updater.getUpdateAsync(trip).test()
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertNoValues()
    }

    @Test
    fun shouldReturnDisplayTrip() {
        val group = mockk<TripGroup>()
        val trip = mockk<Trip>()
        val updateUrl = "https://goo.gl/f2yv7k"
        val displayTrip = mockk<Trip>()
        val response = mockk<RoutingResponse>()

        every { trip.updateURL } returns updateUrl
        every { group.displayTrip } returns displayTrip
        every { response.tripGroupList } returns arrayListOf(group)
        every { api.fetchUpdateAsync(updateUrl) } returns Observable.just(response)

        // Mock processRawData as this seems to be part of the issue
        every { response.processRawData(any(), any()) } just Runs

        val subscriber = TestObserver<Trip>()
        updater.getUpdateAsync(trip).subscribe(subscriber)
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        assertThat(subscriber.events[0]).containsExactly(displayTrip)
    }

    @Test
    fun shouldReturnEmptyIfNoGroupsAvailable() {
        val trip = mockk<Trip>()
        val updateUrl = "https://goo.gl/f2yv7k"
        val response = mockk<RoutingResponse>()

        every { trip.updateURL } returns updateUrl
        every { response.tripGroupList } returns null
        every { api.fetchUpdateAsync(updateUrl) } returns Observable.just(response)

        // Mock processRawData as this seems to be part of the issue
        every { response.processRawData(any(), any()) } just Runs

        val subscriber = updater.getUpdateAsync(trip).test()
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertNoValues()
    }

    @Test
    fun shouldReturnEmptyIfNoTripsAvailable() {
        val trip = mockk<Trip>()
        val updateUrl = "https://goo.gl/f2yv7k"
        val group = mockk<TripGroup>()
        val response = mockk<RoutingResponse>()

        every { trip.updateURL } returns updateUrl
        every { group.displayTrip } returns null
        every { response.tripGroupList } returns arrayListOf(group)
        every { api.fetchUpdateAsync(updateUrl) } returns Observable.just(response)

        // Mock processRawData as this seems to be part of the issue
        every { response.processRawData(any(), any()) } just Runs

        val subscriber = updater.getUpdateAsync(trip).test()
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertNoValues()
    }
}