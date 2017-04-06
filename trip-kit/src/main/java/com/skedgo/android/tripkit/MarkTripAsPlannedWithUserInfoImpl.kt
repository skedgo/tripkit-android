package com.skedgo.android.tripkit

import rx.Completable
import skedgo.tripkit.analytics.UserInfo
import skedgo.tripkit.analytics.convertToMap
import javax.inject.Inject

/**
 * FIXME: Make this impl class internal.
 */
class MarkTripAsPlannedWithUserInfoImpl @Inject internal constructor(
    private val reportingApi: ReportingApi
) : MarkTripAsPlannedWithUserInfo {
  override fun execute(plannedUrl: String, userInfo: UserInfo): Completable
      = reportingApi.reportPlannedTripAsync("$plannedUrl/", userInfo.convertToMap())
      .toCompletable()
}
