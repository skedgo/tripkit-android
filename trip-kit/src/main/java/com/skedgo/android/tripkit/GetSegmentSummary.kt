package com.skedgo.android.tripkit

import com.skedgo.android.common.model.SegmentType
import com.skedgo.android.common.model.Trip
import com.skedgo.android.common.model.TripSegment
import rx.Observable
import javax.inject.Inject


class GetSegmentSummary @Inject constructor() {
  fun execute(trip: Trip): Observable<List<TripSegment>> {
    return Observable.fromCallable <List<TripSegment>> { trip.segments }
        .filter { it != null }
        .defaultIfEmpty(emptyList<TripSegment>())
        .flatMap { Observable.from(it) }
        .filter { it.type != SegmentType.ARRIVAL && it.isVisibleInContext(TripSegment.VISIBILITY_IN_SUMMARY) }
        .toList()
  }
}