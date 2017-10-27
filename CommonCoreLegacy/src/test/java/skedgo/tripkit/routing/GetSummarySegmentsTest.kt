package skedgo.tripkit.routing

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test

class GetSummarySegmentsTest {
  @Test fun arrivalSegmentShouldNotBeInSummaryArea() {
    // Given a trip having arrival segment that is visible on the map.
    val arrivalSegment = TripSegment()
    arrivalSegment.type = SegmentType.ARRIVAL
    arrivalSegment.visibility = Visibilities.VISIBILITY_ON_MAP
    val trip = Trip()
    trip.segments = arrayListOf(arrivalSegment)

    // We expect that arrival segment shouldn't be in the summary area.
    assertThat(trip.getSummarySegments()).isEmpty()
  }

  @Test fun summaryAreaShouldBeEmptyIfNoSegments() {
    val trip = Trip()
    trip.segments = arrayListOf()
    assertThat(trip.getSummarySegments()).isEmpty()
  }

  @Test fun summaryAreaShouldOnlyIncludeSegmentsVisibleOnSummary() {
    val a = mock<TripSegment>()
    whenever(a.isVisibleInContext(eq(Visibilities.VISIBILITY_IN_SUMMARY)))
        .thenReturn(true)

    val b = mock<TripSegment>()
    whenever(b.isVisibleInContext(eq(Visibilities.VISIBILITY_IN_SUMMARY)))
        .thenReturn(false)

    val trip = Trip()
    trip.segments = arrayListOf(a, b)

    // We expect that the summary area should only include
    // segments which are visible on the summary.
    assertThat(trip.getSummarySegments()).containsExactly(a)
  }
}
