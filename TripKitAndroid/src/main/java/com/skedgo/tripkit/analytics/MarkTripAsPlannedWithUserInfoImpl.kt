/* FIXME: Move this class to the TripKitData module. */
package com.skedgo.tripkit.analytics

import io.reactivex.Observable

internal class MarkTripAsPlannedWithUserInfoImpl internal constructor(
    private val markTripAsPlannedApi: MarkTripAsPlannedApi
) : MarkTripAsPlannedWithUserInfo {
    override fun execute(plannedUrl: String, userInfo: MutableMap<String, Any>): Observable<Unit> =
        markTripAsPlannedApi.execute("$plannedUrl/", userInfo)
}
