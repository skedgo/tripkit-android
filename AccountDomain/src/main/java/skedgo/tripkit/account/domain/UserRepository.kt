package skedgo.tripkit.account.domain

import rx.Completable
import rx.Single

interface UserRepository {
  fun getUserKey(): Single<String>
}
