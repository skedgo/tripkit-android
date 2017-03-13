package skedgo.iseventincluded.data

import android.content.SharedPreferences
import rx.Observable
import skedgo.iseventincluded.domain.IsEventIncluded
import skedgo.iseventincluded.domain.IsEventIncludedRepository

internal class IsEventIncludedRepositoryImpl constructor(
    private val preferences: SharedPreferences
) : IsEventIncludedRepository {
  override fun setIsEventIncluded(
      eventId: String,
      isEventIncluded: IsEventIncluded): Observable<Unit>
      = Observable.fromCallable {
    preferences.edit()
        .putString(eventId, isEventIncluded.name)
        .apply()
  }

  override fun getIsEventIncluded(eventId: String): Observable<IsEventIncluded>
      = Observable.fromCallable {
    val r: String? = preferences.getString(eventId, null)
    when (r) {
      IsEventIncluded.YES.name -> IsEventIncluded.YES
      IsEventIncluded.NO.name -> IsEventIncluded.NO
      else -> IsEventIncluded.NOT_SPECIFIED_YET
    }
  }
}
