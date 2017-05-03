package com.skedgo.android.common.util

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.skedgo.android.common.BuildConfig
import com.skedgo.android.common.TestRunner
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import skedgo.tripkit.routing.TripSegment
import skedgo.tripkit.routing.startDateTime

@RunWith(TestRunner::class)
@Config(constants = BuildConfig::class)
class TripSegmentUtilsTest {

  @Test
  fun shouldReplaceDurationTemplate() {
    val segment: TripSegment = mock()
    whenever(segment.action).thenReturn("Ride Taxi <DURATION>")
    whenever(segment.startTimeInSecs).thenReturn(1000)
    whenever(segment.endTimeInSecs).thenReturn(1060)
    val action = TripSegmentUtils.getTripSegmentAction(RuntimeEnvironment.application, segment)

    assertThat(action).isEqualTo("Ride Taxi for 1min")
  }

}