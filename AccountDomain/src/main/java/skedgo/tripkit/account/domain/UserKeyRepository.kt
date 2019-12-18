package skedgo.tripkit.account.domain

import io.reactivex.Completable
import io.reactivex.Single

interface UserKeyRepository {
  fun getUserKey(): Single<String>
}
