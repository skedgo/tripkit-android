/* FIXME: Move this class to the TripKitData module. */
package com.skedgo.tripkit.analytics

import io.reactivex.Completable
import skedgo.tripkit.analytics.MarkTripAsPlannedWithUserInfo


internal class MarkTripAsPlannedWithUserInfoImpl internal constructor(
    private val markTripAsPlannedApi: MarkTripAsPlannedApi
) : MarkTripAsPlannedWithUserInfo {
  override fun execute(plannedUrl: String, userInfo: MutableMap<String, Any>): Completable
      = markTripAsPlannedApi.execute("$plannedUrl/", userInfo).ignoreElements()
}
