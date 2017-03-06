package skedgo.tripkit.account.domain

import rx.Observable

interface UserRepository {
  fun getUser(): Observable<User>
}
