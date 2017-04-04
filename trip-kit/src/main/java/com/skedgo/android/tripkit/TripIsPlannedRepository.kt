package com.skedgo.android.tripkit

import rx.Completable
import skedgo.tripkit.analytics.UserInfo

interface TripIsPlannedRepository {

  fun markPlannedTrip(plannedUrl: String): Completable
  fun markPlannedTrip(plannedUrl: String, userInfo: UserInfo) : Completable
}