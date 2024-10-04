package com.skedgo.tripkit.common.util

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.routing.TripSegment
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TripSegmentUtilsTest {
  @Test
  fun shouldReplaceDurationTemplate() {
    // Mock the TripSegment object using MockK
    val segment: TripSegment = mockk()

    // Mock the behavior of the segment object
    every { segment.action } returns "Ride Taxi<DURATION>"
    every { segment.startTimeInSecs } returns 1000L
    every { segment.endTimeInSecs } returns 1060L
    every { segment.timeZone } returns null

    // Call the method under test
    val action = TripSegmentUtils
      .getTripSegmentAction(ApplicationProvider.getApplicationContext(), segment)

    // Assert the expected outcome
    assertThat(action).isEqualTo("Ride Taxi for 1mins")
  }

}
