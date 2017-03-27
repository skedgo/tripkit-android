package skedgo.tripkit.demo.a2btrips

import com.skedgo.android.common.model.Trip
import rx.Observable


class GetSegmentsSummary {
  fun execute(trip: Trip): Observable<String> {
    return Observable.fromCallable {
      trip.segments
    }.flatMap { Observable.from(it) }
        .filter { it.modeInfo != null }
        .map { it.modeInfo!!.alternativeText }
  }
}