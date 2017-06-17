package com.skedgo.android.tripkit.bookingproviders

import android.content.Intent
import com.skedgo.android.common.model.Location
import com.skedgo.android.tripkit.TripKitAndroidRobolectricTest
import com.skedgo.android.tripkit.ExternalActionParams
import org.amshove.kluent.*
import org.junit.Test
import rx.functions.Func1
import skedgo.tripkit.routing.TripSegment

@Suppress("IllegalIdentifier")
class UberBookingResolverTest : TripKitAndroidRobolectricTest() {
  val isPackageInstalled: Func1<String, Boolean> = mock()
  val getAppIntent: Func1<String, Intent> = mock()
  val resolver by lazy { UberBookingResolver(isPackageInstalled, getAppIntent) }

  @Test fun `title should be null`() {
    resolver.getTitleForExternalAction("any action") `should be` null
  }

  @Test fun `should return Intent to launch Uber app directly`() {
    val params: ExternalActionParams = mock()

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
    When calling isPackageInstalled.call(UBER_PACKAGE) itReturns true
    When calling getAppIntent.call(UBER_PACKAGE) itReturns Intent()

    val subscriber = resolver
        .performExternalActionAsync(params)
        .test()
        .awaitTerminalEvent()
        .assertNoErrors()

    val bookingAction = subscriber.onNextEvents[0]
    bookingAction.data() `should not be` null
    bookingAction.data().data.toString() `should equal` "uber://?action=setPickup&pickup[latitude]=1.0&pickup[longitude]=2.0&dropoff[latitude]=3.0&dropoff[longitude]=4.0"
    bookingAction.hasApp() `should be` true
  }

  @Test fun `should return Intent to get Uber app from Play store`() {
    val params: ExternalActionParams = mock()

    When calling isPackageInstalled.call(UBER_PACKAGE) itReturns false

    val subscriber = resolver
        .performExternalActionAsync(params)
        .test()
        .awaitTerminalEvent()
        .assertNoErrors()

    val bookingAction = subscriber.onNextEvents[0]
    bookingAction.data() `should not be` null
    bookingAction.data().data.toString() `should equal` "https://play.google.com/store/apps/details?id=com.ubercab"
    bookingAction.hasApp() `should be` false
  }
}