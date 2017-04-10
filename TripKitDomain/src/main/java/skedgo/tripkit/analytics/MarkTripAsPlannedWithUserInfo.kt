package skedgo.tripkit.analytics

import rx.Completable

/**
 * Example use-case: Mark a trip as planned, and
 * then later, get push notifications about alerts relevant to the trip,
 * or about ride sharing opportunities.
 */
interface MarkTripAsPlannedWithUserInfo {
  /**
   * @param userInfo Represents an arbitrary data which must be able to be serialized to JSON.
   */
  fun execute(plannedUrl: String, userInfo: MutableMap<String, Any>): Completable
}
