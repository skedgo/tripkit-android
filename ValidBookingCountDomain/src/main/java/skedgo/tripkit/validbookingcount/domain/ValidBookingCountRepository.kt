package skedgo.tripkit.validbookingcount.domain

import io.reactivex.Observable

interface ValidBookingCountRepository {
  fun getLocalValidBookingCount(): Observable<Int>
  fun getRemoteValidBookingCount(): Observable<Int>
}
