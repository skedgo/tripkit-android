package skedgo.tripkit.configuration

import io.reactivex.Observable

interface AppVersionNameRepository {
  fun getAppVersionName(): Observable<String>
}
