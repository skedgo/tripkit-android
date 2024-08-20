package com.skedgo.tripkit.routing

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GetSummarySegmentsTest {
    @Test
    fun arrivalSegmentShouldNotBeInSummaryArea() {
        // Given a trip having arrival segment that is visible on the map.
        val arrivalSegment = TripSegment()
        arrivalSegment.type = SegmentType.ARRIVAL
        arrivalSegment.visibility = Visibilities.VISIBILITY_ON_MAP
        val trip = Trip()
        trip.segments = arrayListOf(arrivalSegment)

        // We expect that arrival segment shouldn't be in the summary area.
        assertThat(trip.getSummarySegments()).isEmpty()
    }

    @Test
    fun summaryAreaShouldBeEmptyIfNoSegments() {
        val trip = Trip()
        trip.segments = arrayListOf()
        assertThat(trip.getSummarySegments()).isEmpty()
    }

    @Test
    fun summaryAreaShouldOnlyIncludeSegmentsVisibleOnSummary() {
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
