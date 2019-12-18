package com.skedgo.tripkit.account.domain

import io.reactivex.Single

interface UserKeyRepository {
  fun getUserKey(): Single<String>
}
