package com.skedgo.android.tripkit

import com.skedgo.android.common.model.Trip
import com.skedgo.android.common.model.TripSegment
import rx.Observable


class GetSegmentsSummary {
  fun execute(trip: Trip): Observable<List<TripSegment>> {
    return Observable.fromCallable { trip.summarySegments }
  }
}