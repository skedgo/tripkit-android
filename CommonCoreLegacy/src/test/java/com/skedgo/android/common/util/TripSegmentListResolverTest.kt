package com.skedgo.android.common.util

import com.skedgo.android.common.BuildConfig
import com.skedgo.android.common.TestRunner
import com.skedgo.android.common.model.SegmentType
import com.skedgo.android.common.model.TripSegment
import com.skedgo.android.common.model.TripSegment.VISIBILITY_IN_DETAILS
import com.skedgo.android.common.model.TripSegment.VISIBILITY_ON_MAP
import org.assertj.core.api.Assertions.assertThat
import org.joda.time.DateTime
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import skedgo.tripkit.routing.toSeconds

@RunWith(TestRunner::class)
@Config(constants = BuildConfig::class)
class TripSegmentListResolverTest {
  private val resolver by lazy {
    TripSegmentListResolver(RuntimeEnvironment.application.resources)
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
