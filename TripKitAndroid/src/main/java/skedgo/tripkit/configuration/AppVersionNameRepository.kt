package skedgo.tripkit.configuration

import rx.Observable

interface AppVersionNameRepository {
  fun getAppVersionName(): Observable<String>
}
