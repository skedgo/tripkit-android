package com.skedgo.android.tripkit

import rx.Completable
import skedgo.tripkit.analytics.UserInfo
import skedgo.tripkit.analytics.convertToMap
import javax.inject.Inject

class TripIsPlannedRepositoryImpl @Inject internal constructor(private val reportingApi: ReportingApi) : TripIsPlannedRepository {

  override fun markPlannedTrip(plannedUrl: String) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun markPlannedTrip(plannedUrl: String, userInfo: UserInfo): Completable {
    return reportingApi.reportPlannedTripAsync(plannedUrl, userInfo.convertToMap())
        .toCompletable()
  }
}