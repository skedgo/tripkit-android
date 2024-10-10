package com.skedgo.tripkit.bookingproviders

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.TripKitAndroidRobolectricTest
import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.routing.TripSegment
import io.reactivex.functions.Function
import org.amshove.kluent.When
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.amshove.kluent.mock
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not be`
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("IllegalIdentifier")
@RunWith(AndroidJUnit4::class)
class UberBookingResolverTest : TripKitAndroidRobolectricTest() {
    val isPackageInstalled: Function<String, Boolean> = mock()
    val getAppIntent: Function<String, Intent> = mock()
    val resolver by lazy { UberBookingResolver(isPackageInstalled, getAppIntent) }

    @Test
    fun `title should be null`() {
        resolver.getTitleForExternalAction("any action") `should be` null
    }

    @Test
    fun `should return Intent to launch Uber app directly`() {
        val params: com.skedgo.tripkit.ExternalActionParams = mock()

        val origin: Location = mock()
        When calling origin.lat itReturns 1.0
        When calling origin.lon itReturns 2.0

        val destination: Location = mock()
        When calling destination.lat itReturns 3.0
        When calling destination.lon itReturns 4.0

        val segment: TripSegment = mock()
        When calling segment.from itReturns origin
        When calling segment.to itReturns destination
        When calling params.segment() itReturns segment
        When calling isPackageInstalled.apply(UBER_PACKAGE) itReturns true
        When calling getAppIntent.apply(UBER_PACKAGE) itReturns Intent()

        val subscriber = resolver
            .performExternalActionAsync(params)
            .test()
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()

        val bookingAction = subscriber.events[0].first() as com.skedgo.tripkit.BookingAction
        bookingAction.data() `should not be` null
        bookingAction.data().data.toString() `should equal` "uber://?action=setPickup&pickup[latitude]=1.0&pickup[longitude]=2.0&dropoff[latitude]=3.0&dropoff[longitude]=4.0"
        bookingAction.hasApp() `should be` true
    }

    @Test
    fun `should return Intent to get Uber app from Play store`() {
        val params: com.skedgo.tripkit.ExternalActionParams = mock()

        When calling isPackageInstalled.apply(UBER_PACKAGE) itReturns false

        val subscriber = resolver
            .performExternalActionAsync(params)
            .test()

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()

        val bookingAction = subscriber.events[0].first() as com.skedgo.tripkit.BookingAction
        bookingAction.data() `should not be` null
        bookingAction.data().data.toString() `should equal` "https://play.google.com/store/apps/details?id=com.ubercab"
        bookingAction.hasApp() `should be` false
    }
}