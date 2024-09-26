package com.skedgo.tripkit.a2brouting

import android.os.Parcel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.TransportModeFilter
import com.skedgo.tripkit.booking.ui.base.MockKTest
import com.skedgo.tripkit.common.model.Query
import com.skedgo.tripkit.routing.TripGroup
import io.mockk.every
import io.mockk.mockk
import io.reactivex.subjects.PublishSubject
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit

@RunWith(AndroidJUnit4::class)
class SingleRouteServiceTest: MockKTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val routeService: RouteService = mockk()

    internal var transportModeFilter = object : TransportModeFilter {
        override fun writeToParcel(dest: Parcel, flags: Int) {

        }

        override fun describeContents(): Int {
            return 0
        }

        override fun getFilteredMode(originalModes: List<String>): List<String> = emptyList()
    }
    private lateinit var singleRouteService: SingleRouteService

    @Before
    fun before() {
        initRx()
        singleRouteService = SingleRouteService(routeService)
    }

    @After
    fun after() {
        tearDownRx()
    }

    /**
     * Given we've spawned a routing request (A) and
     * later spawned another routing request (B),
     * we expect that A should be cancelled before B is spawned.
     */
    @Test
    fun shouldCancelPreviousRequest_withQuery() {
        val emitter1 = PublishSubject.create<List<TripGroup>>()
        val emitter2 = PublishSubject.create<List<TripGroup>>()

        // Mock the routeService and set up the stubbing
        every { routeService.routeAsync(any(), eq(transportModeFilter)) }
            .returnsMany(emitter1, emitter2)

        // Mock the Query class
        val queryMock = mockk<Query>(relaxed = true)

        // Subscribe to the first emitter
        singleRouteService.routeAsync(queryMock, transportModeFilter).subscribe()
        assertThat(emitter1.hasObservers()).isTrue()

        // Subscribe to the second emitter
        singleRouteService.routeAsync(queryMock, transportModeFilter).subscribe()
        assertThat(emitter1.hasObservers()).isFalse()
        assertThat(emitter2.hasObservers()).isTrue()
    }
}