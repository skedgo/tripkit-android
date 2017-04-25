/* FIXME: Move this class to the TripKitData module. */
package skedgo.tripkit.analytics

import rx.Completable

internal class MarkTripAsPlannedWithUserInfoImpl internal constructor(
    private val markTripAsPlannedApi: MarkTripAsPlannedApi
) : MarkTripAsPlannedWithUserInfo {
  override fun execute(plannedUrl: String, userInfo: MutableMap<String, Any>): Completable
      = markTripAsPlannedApi.execute("$plannedUrl/", userInfo)
      .toCompletable()
}
