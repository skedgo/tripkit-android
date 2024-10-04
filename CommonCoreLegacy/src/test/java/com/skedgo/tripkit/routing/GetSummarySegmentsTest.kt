package com.skedgo.tripkit.routing

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GetSummarySegmentsTest {
    @Test
    fun arrivalSegmentShouldNotBeInSummaryArea() {
        // Given a trip having an arrival segment that is visible on the map.
        val arrivalSegment = TripSegment()
        arrivalSegment.setType(SegmentType.ARRIVAL)
        arrivalSegment.visibility = Visibilities.VISIBILITY_ON_MAP
        val trip = Trip()
        trip.segmentList = arrayListOf(arrivalSegment)

        // We expect that the arrival segment shouldn't be in the summary area.
        assertThat(trip.getSummarySegments()).isEmpty()
    }

    @Test
    fun summaryAreaShouldBeEmptyIfNoSegments() {
        val trip = Trip()
        trip.segmentList = arrayListOf()
        assertThat(trip.getSummarySegments()).isEmpty()
    }

    @Test
    fun summaryAreaShouldOnlyIncludeSegmentsVisibleOnSummary() {
        // Mocking segments with MockK
        val a = mockk<TripSegment>(relaxed = true)
        a.setType(SegmentType.ARRIVAL)
        every { a.isVisibleInContext(eq(Visibilities.VISIBILITY_IN_SUMMARY)) } returns true

        val b = mockk<TripSegment>(relaxed = true)
        b.setType(SegmentType.ARRIVAL)
        every { b.isVisibleInContext(eq(Visibilities.VISIBILITY_IN_SUMMARY)) } returns false

        every { a.trip } returns mockk<Trip>()
        every { b.trip } returns mockk<Trip>()

        val trip = Trip()
        trip.segmentList = arrayListOf(a, b)

        // We expect that the summary area should only include
        // segments which are visible on the summary.
        assertThat(trip.getSummarySegments()).containsExactly(a)
    }
}
