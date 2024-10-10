package com.skedgo.tripkit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.routing.Trip
import com.skedgo.tripkit.routing.TripGroup
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class PeriodicRealTimeTripUpdateReceiverTest {

    private lateinit var tripUpdater: TripUpdater
    private lateinit var group: TripGroup
    private lateinit var receiver: RealTimeTripUpdateReceiver

    @Before
    fun before() {
        tripUpdater = mockk()
        group = mockk()

        receiver = PeriodicRealTimeTripUpdateReceiverBuilder()
            .tripUpdater(tripUpdater)
            .group(group)
            .initialDelay(1)
            .period(1)
            .timeUnit(TimeUnit.SECONDS)
            .build()
    }

    @Test
    fun ignoreErrorByTripUpdater() {
        val displayTrip = mockk<Trip>()

        // Mocking the displayTrip's updateURL and group's displayTrip
        every { displayTrip.updateURL } returns "AUG 2016"
        every { group.displayTrip } returns displayTrip

        // Mocking the behavior of tripUpdater's getUpdateAsync to return an error
        every { tripUpdater.getUpdateAsync("AUG 2016") } returns Observable.error(RuntimeException())

        val subscriber = receiver.startAsync().test()
        subscriber.awaitTerminalEvent(3, TimeUnit.SECONDS)
        receiver.stop()
        subscriber.assertTerminated()
        subscriber.assertNoErrors()
    }
}