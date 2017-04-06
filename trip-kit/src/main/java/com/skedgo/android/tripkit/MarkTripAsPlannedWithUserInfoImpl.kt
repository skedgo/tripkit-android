package com.skedgo.android.tripkit

import rx.Completable
import skedgo.tripkit.analytics.MarkTripAsPlannedWithUserInfo

internal class MarkTripAsPlannedWithUserInfoImpl internal constructor(
    private val markTripAsPlannedApi: MarkTripAsPlannedApi
) : MarkTripAsPlannedWithUserInfo {
  override fun execute(plannedUrl: String, userInfo: MutableMap<String, Any>): Completable
      = markTripAsPlannedApi.execute("$plannedUrl/", userInfo)
      .toCompletable()
}
