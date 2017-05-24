package com.skedgo.android.tripkit.bookingproviders

import android.content.Intent
import com.skedgo.android.common.model.Location
import com.skedgo.android.tripkit.BaseUnitTest
import com.skedgo.android.tripkit.BookingAction
import com.skedgo.android.tripkit.ExternalActionParams
import org.amshove.kluent.*
import org.junit.Test
import rx.functions.Func1
import rx.observers.TestSubscriber
import skedgo.tripkit.routing.TripSegment

@Suppress("IllegalIdentifier")
class UberBookingResolverTest : BaseUnitTest() {

  val isPackageInstalled: Func1<String, Boolean> = mock()
  val getAppIntent: Func1<String, Intent> = mock()

  val getTripGroupActionType by lazy { UberBookingResolver(isPackageInstalled, getAppIntent) }

  @Test fun `should be null title`() {
    getTripGroupActionType.getTitleForExternalAction("any action") `should be` null
  }

  @Test fun `should be uber app intent`() {

    val params: ExternalActionParams = mock()

    val locFrom: Location = mock()
    When calling locFrom.lat itReturns 1.0
    When calling locFrom.lon itReturns 2.0

    val locTo: Location = mock()
    When calling locTo.lat itReturns 3.0
    When calling locTo.lon itReturns 4.0

    val segment: TripSegment = mock()
    When calling segment.from itReturns locFrom
    When calling segment.to itReturns locTo

    When calling params.segment() itReturns segment

    When calling isPackageInstalled.call(UBER_PACKAGE) itReturns true
    When calling getAppIntent.call(UBER_PACKAGE) itReturns Intent()

    val subscriber = TestSubscriber<BookingAction>()

    getTripGroupActionType.performExternalActionAsync(params).subscribe(subscriber)

    subscriber.assertNoErrors()

    val bookingAction = subscriber.onNextEvents[0]

    bookingAction.data() `should not be` null
    bookingAction.data().data.toString() `should equal` "uber://?action=setPickup&pickup[latitude]=1.0&pickup[longitude]=2.0&dropoff[latitude]=3.0&dropoff[longitude]=4.0"
    bookingAction.hasApp() `should be` true

  }

  @Test fun `should be uber play store intent`() {

    val params: ExternalActionParams = mock()

    When calling isPackageInstalled.call(UBER_PACKAGE) itReturns false

    val subscriber = TestSubscriber<BookingAction>()

    getTripGroupActionType.performExternalActionAsync(params).subscribe(subscriber)

    subscriber.assertNoErrors()

    val bookingAction = subscriber.onNextEvents[0]

    bookingAction.data() `should not be` null
    bookingAction.data().data.toString() `should equal` "https://play.google.com/store/apps/details?id=" + UBER_PACKAGE
    bookingAction.hasApp() `should be` false

  }

}