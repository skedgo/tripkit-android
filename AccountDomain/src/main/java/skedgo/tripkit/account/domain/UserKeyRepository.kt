package skedgo.tripkit.account.domain

import rx.Completable
import rx.Single

interface UserKeyRepository {
  fun getUserKey(): Single<String>
}
