package com.skedgo.android.tripkit

import rx.Completable
import skedgo.tripkit.analytics.UserInfo

interface MarkTripAsPlannedWithUserInfo {
  /**
   * Reports the provided trip as being planned for the user.
   * This is posted to the server with additional optional data.
   * Example use case: Report a trip as being planned, and
   * then later get push notifications about alerts relevant to the trip,
   * or about ride sharing opportunities.
   */
  fun execute(plannedUrl: String, userInfo: UserInfo): Completable
}
