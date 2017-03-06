package skedgo.tripkit.account.domain

import rx.Observable

interface UserNameRepository {
  fun getUserName(): Observable<String>
  fun setUserName(userName: String?): Observable<Unit>
}
