package com.skedgo.tripkit.common.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.assertj.core.api.Java6Assertions.assertThat
import org.joda.time.DateTime
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import com.skedgo.tripkit.routing.SegmentType
import com.skedgo.tripkit.routing.TripSegment
import com.skedgo.tripkit.routing.Visibilities.VISIBILITY_IN_DETAILS
import com.skedgo.tripkit.routing.Visibilities.VISIBILITY_ON_MAP
import com.skedgo.tripkit.routing.toSeconds

@RunWith(RobolectricTestRunner::class)
class TripSegmentListResolverTest {
  private val resolver by lazy {
    TripSegmentListResolver(ApplicationProvider.getApplicationContext<Context>().resources)
  }

  @Test fun shouldOnlyShowDepartureSegmentInDetails() {
    val segment = TripSegment()
    val departureSegment = resolver.createDepartureSegment(segment)
    assertThat(departureSegment.visibility).isEqualTo(VISIBILITY_IN_DETAILS)
  }

  @Test fun arrivalSegmentShouldBeVisibleInMap() {
    val segment = TripSegment()
    val arrivalSegment = resolver.createArrivalSegment(segment)
    assertThat(arrivalSegment.visibility).isEqualTo(VISIBILITY_ON_MAP)
  }

  @Test fun arrivalSegmentShouldHaveCorrectType() {
    val segment = TripSegment()
    val arrivalSegment = resolver.createArrivalSegment(segment)
    assertThat(arrivalSegment.type).isEqualTo(SegmentType.ARRIVAL)
  }

  @Test fun departureSegmentShouldHaveCorrectType() {
    val segment = TripSegment()
    val departureSegment = resolver.createDepartureSegment(segment)
    assertThat(departureSegment.type).isEqualTo(SegmentType.DEPARTURE)
  }

  @Test fun shouldHaveCorrectTimeRangeForArrivalSegment() {
    val lastSegment = TripSegment()
    lastSegment.startTimeInSecs = DateTime.parse("2012-06-30T00:00").toSeconds()
    lastSegment.endTimeInSecs = DateTime.parse("2012-06-30T00:30").toSeconds()

    val arrivalSegment = resolver.createArrivalSegment(lastSegment)

    assertThat(arrivalSegment.startTimeInSecs).isEqualTo(lastSegment.endTimeInSecs)
    assertThat(arrivalSegment.endTimeInSecs).isEqualTo(lastSegment.endTimeInSecs)
  }

  @Test fun shouldHaveCorrectTimeRangeForDepartureSegment() {
    val firstSegment = TripSegment()
    firstSegment.startTimeInSecs = DateTime.parse("2012-06-30T00:00").toSeconds()
    firstSegment.endTimeInSecs = DateTime.parse("2012-06-30T00:30").toSeconds()

    val departureSegment = resolver.createDepartureSegment(firstSegment)

    assertThat(departureSegment.startTimeInSecs).isEqualTo(firstSegment.startTimeInSecs)
    assertThat(departureSegment.endTimeInSecs).isEqualTo(firstSegment.startTimeInSecs)
  }
}
