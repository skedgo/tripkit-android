package com.skedgo.android.tripkit

import com.skedgo.android.common.model.SegmentType
import com.skedgo.android.common.model.Trip
import com.skedgo.android.common.model.TripSegment
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import rx.observers.TestSubscriber

class GetSegmentSummaryTest {

  val getSegmentSummary = GetSegmentSummary()

  @Test fun arrivalSegmentShouldNotBeInSummaryArea() {
    // Given a trip having arrival segment that is visible on the map.
    val arrivalSegment = TripSegment()
    arrivalSegment.type = SegmentType.ARRIVAL
    arrivalSegment.visibility = TripSegment.VISIBILITY_ON_MAP
    val trip = Trip()
    trip.segments = arrayListOf(arrivalSegment)

    // We expect that arrival segment shouldn't be in the summary area.
    val tripSegmentSubscriber = TestSubscriber.create<List<TripSegment>>()
    getSegmentSummary.execute(trip)
        .subscribe(tripSegmentSubscriber)
    tripSegmentSubscriber.assertValueCount(1)
    assertThat(tripSegmentSubscriber.onNextEvents.first()).isEmpty()
  }

  @Test fun summaryAreaShouldBeEmptyIfNoSegments() {
    val trip = Mockito.mock(Trip::class.java)
    `when`(trip.segments).thenReturn(arrayListOf())
    val tripSegmentSubscriber = TestSubscriber.create<List<TripSegment>>()
    getSegmentSummary.execute(trip)
        .subscribe(tripSegmentSubscriber)
    tripSegmentSubscriber.assertValueCount(1)
    assertThat(tripSegmentSubscriber.onNextEvents.first()).isEmpty()
  }

  @Test fun summaryAreaShouldOnlyIncludeSegmentsVisibleOnSummary() {
    val a: TripSegment = Mockito.mock(TripSegment::class.java)
    `when`<Boolean>(a.isVisibleInContext(Matchers.eq(TripSegment.VISIBILITY_IN_SUMMARY)))
        .thenReturn(true)
    val b: TripSegment = Mockito.mock(TripSegment::class.java)
    `when`<Boolean>(b.isVisibleInContext(Matchers.eq(TripSegment.VISIBILITY_IN_SUMMARY)))
        .thenReturn(false)
    val trip: Trip = Mockito.mock(Trip::class.java)
    `when`(trip.segments).thenReturn(arrayListOf(a, b))

    // We expect that the summary area should only include
    // segments which are visible on the summary.
    val tripSegmentSubscriber = TestSubscriber.create<List<TripSegment>>()
    getSegmentSummary.execute(trip)
        .subscribe(tripSegmentSubscriber)
    tripSegmentSubscriber.assertValueCount(1)
    assertThat(tripSegmentSubscriber.onNextEvents.first()).containsExactly(a)
  }
}