package skedgo.tripkit.account.domain

import rx.Completable
import rx.Single

interface UserRepository {
  fun getUserKey(): Single<String>
  fun setUserKey(userKey: String): Completable
}
