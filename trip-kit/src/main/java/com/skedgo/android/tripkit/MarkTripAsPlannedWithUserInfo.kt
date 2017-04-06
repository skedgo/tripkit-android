package com.skedgo.android.tripkit

import rx.Completable
import skedgo.tripkit.analytics.UserInfo

/**
 * Example use-case: Mark a trip as planned, and
 * then later, get push notifications about alerts relevant to the trip,
 * or about ride sharing opportunities.
 */
interface MarkTripAsPlannedWithUserInfo {
  fun execute(plannedUrl: String, userInfo: UserInfo): Completable
}
