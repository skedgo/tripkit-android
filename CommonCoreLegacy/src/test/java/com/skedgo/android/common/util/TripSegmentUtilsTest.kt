package com.skedgo.android.common.util

import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.android.common.TestRunner
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import skedgo.tripkit.routing.TripSegment

@RunWith(TestRunner::class)
class TripSegmentUtilsTest {

  @Test
  fun shouldReplaceDurationTemplate() {
    val segment: TripSegment = mock()
    whenever(segment.action).thenReturn("Ride Taxi<DURATION>")
    whenever(segment.startTimeInSecs).thenReturn(1000)
    whenever(segment.endTimeInSecs).thenReturn(1060)
    val action = TripSegmentUtils.getTripSegmentAction(ApplicationProvider.getApplicationContext(), segment)

    assertThat(action).isEqualTo("Ride Taxi for 1min")
  }

}