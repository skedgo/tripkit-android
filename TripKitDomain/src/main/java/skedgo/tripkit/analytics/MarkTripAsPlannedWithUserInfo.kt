package skedgo.tripkit.analytics

import io.reactivex.Completable


/**
 * Example use-case: Mark a trip as planned, and
 * then later, get push notifications about alerts relevant to the trip,
 * or about ride sharing opportunities.
 */
interface MarkTripAsPlannedWithUserInfo {
  /**
   * @param userInfo This parameter is to optionally attach a kind of arbitrary data
   * which represents user preferences. `userInfo` must be able to be serialized to JSON.
   */
  fun execute(plannedUrl: String, userInfo: MutableMap<String, Any>): Completable
}
