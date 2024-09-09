package com.skedgo.tripkit

import android.content.res.Resources
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.routing.RoutingResponse
import com.skedgo.tripkit.routing.Trip
import com.skedgo.tripkit.routing.TripGroup
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TripUpdaterImplTest {
    @Mock
    internal var resources: Resources? = null

    @Mock
    internal var api: TripUpdateApi? = null
    private var updater: TripUpdaterImpl? = null

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        updater = TripUpdaterImpl(resources!!, api!!, Gson())
    }

    @Test
    fun shouldReturnEmpty() {
        val trip = mock(Trip::class.java)
        val subscriber = TestObserver<Trip>()
        updater!!.getUpdateAsync(trip).subscribe(subscriber)
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertNoValues()
    }

    @Test
    fun shouldReturnDisplayTrip() {
        val group = mock(TripGroup::class.java)
        val trip = mock(Trip::class.java)
        val updateUrl = "https://goo.gl/f2yv7k"
        whenever(trip.updateURL).thenReturn(updateUrl)
        val displayTrip = mock(Trip::class.java)
        whenever(group.displayTrip).thenReturn(displayTrip)
        val response = mock(RoutingResponse::class.java)
        whenever(response.tripGroupList)
            .thenReturn(ArrayList(listOf(group)))
        whenever(api!!.fetchUpdateAsync(updateUrl))
            .thenReturn(Observable.just(response))

        val subscriber = TestObserver<Trip>()
        updater!!.getUpdateAsync(trip).subscribe(subscriber)
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        assertThat(subscriber.events[0]).containsExactly(displayTrip)
    }

    @Test
    fun shouldReturnEmptyIfNoGroupsAvailable() {
        val trip = mock(Trip::class.java)
        val updateUrl = "https://goo.gl/f2yv7k"
        whenever(trip.updateURL).thenReturn(updateUrl)
        val response = mock(RoutingResponse::class.java)
        whenever(response.tripGroupList).thenReturn(null)
        whenever(api!!.fetchUpdateAsync(updateUrl))
            .thenReturn(Observable.just(response))

        val subscriber = updater!!.getUpdateAsync(trip).test()
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertNoValues()
    }

    @Test
    fun shouldReturnEmptyIfNoTripsAvailable() {
        val trip = mock(Trip::class.java)
        val updateUrl = "https://goo.gl/f2yv7k"
        whenever(trip.updateURL).thenReturn(updateUrl)
        val group = mock(TripGroup::class.java)
        whenever(group.displayTrip).thenReturn(null)
        val response = mock(RoutingResponse::class.java)
        whenever(response.tripGroupList)
            .thenReturn(ArrayList(listOf(group)))
        whenever(api!!.fetchUpdateAsync(updateUrl))
            .thenReturn(Observable.just(response))

        val subscriber = updater!!.getUpdateAsync(trip).test()
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertNoValues()
    }
}