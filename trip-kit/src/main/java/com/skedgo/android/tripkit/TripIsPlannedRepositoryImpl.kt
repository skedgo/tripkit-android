package com.skedgo.android.tripkit

import rx.Completable
import skedgo.tripkit.analytics.UserInfo
import skedgo.tripkit.analytics.convertToMap
import javax.inject.Inject

class TripIsPlannedRepositoryImpl @Inject internal constructor(
    private val reportingApi: ReportingApi
) : TripIsPlannedRepository {
  override fun markPlannedTrip(plannedUrl: String): Completable {
    TODO("Not implemented")
  }

  override fun markPlannedTrip(plannedUrl: String, userInfo: UserInfo): Completable
      = reportingApi.reportPlannedTripAsync(plannedUrl, userInfo.convertToMap())
      .toCompletable()
}
