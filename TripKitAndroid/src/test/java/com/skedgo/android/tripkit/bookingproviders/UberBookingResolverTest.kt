package com.skedgo.android.tripkit.bookingproviders

import android.content.Intent
import com.skedgo.android.common.BuildConfig
import com.skedgo.android.common.TestRunner
import com.skedgo.android.common.model.Location
import com.skedgo.android.tripkit.BookingAction
import com.skedgo.android.tripkit.ExternalActionParams
import org.amshove.kluent.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import rx.functions.Func1
import rx.observers.TestSubscriber
import skedgo.tripkit.routing.TripSegment

@Suppress("IllegalIdentifier")
@RunWith(TestRunner::class)
@Config(constants = BuildConfig::class)
class UberBookingResolverTest {

  val isPackageInstalled: Func1<String, Boolean> = mock()
  val getAppIntent: Func1<String, Intent> = mock()

  val getTripGroupActionType by lazy { UberBookingResolver(isPackageInstalled, getAppIntent) }

  @Test fun `should be null title`() {
    getTripGroupActionType.getTitleForExternalAction("any action") `should be` null
  }

  @Test fun `should be quick booking type`() {

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
    bookingAction.data().toString() `should be` "uber://?action=setPickup" +
        "&pickup[latitude]=${segment.from.lat}" +
        "&pickup[longitude]=${segment.from.lon}" +
        "&dropoff[latitude]=${segment.to.lat}" +
        "&dropoff[longitude]=${segment.to.lon}"
    bookingAction.hasApp() `should not be` true

  }


}