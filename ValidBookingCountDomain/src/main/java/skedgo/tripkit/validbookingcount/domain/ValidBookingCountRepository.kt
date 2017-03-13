package skedgo.tripkit.validbookingcount.domain

import rx.Observable

interface ValidBookingCountRepository {
  fun getLocalValidBookingCount(): Observable<Int>
  fun getRemoteValidBookingCount(): Observable<Int>
}
