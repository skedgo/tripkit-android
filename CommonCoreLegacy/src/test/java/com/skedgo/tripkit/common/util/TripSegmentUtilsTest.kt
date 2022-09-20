package com.skedgo.tripkit.common.util

import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import com.skedgo.tripkit.routing.TripSegment

/*
@RunWith(RobolectricTestRunner::class)
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

}*/
