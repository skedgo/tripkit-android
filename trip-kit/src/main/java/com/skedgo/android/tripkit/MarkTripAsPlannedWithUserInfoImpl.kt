package com.skedgo.android.tripkit

import rx.Completable
import skedgo.tripkit.analytics.UserInfo
import skedgo.tripkit.analytics.toMap
import javax.inject.Inject

internal class MarkTripAsPlannedWithUserInfoImpl @Inject internal constructor(
    private val markTripAsPlannedApi: MarkTripAsPlannedApi
) : MarkTripAsPlannedWithUserInfo {
  override fun execute(plannedUrl: String, userInfo: UserInfo): Completable
      = markTripAsPlannedApi.execute("$plannedUrl/", userInfo.toMap())
      .toCompletable()
}
