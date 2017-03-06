package skedgo.tripkit.account.domain

import rx.Observable
import javax.inject.Inject

open class HasUserToken @Inject constructor(
    private val userTokenRepository: UserTokenRepository
) {
  open fun execute(): Observable<Boolean>
      = userTokenRepository.getLastKnownUserToken()
      .map { true }
      .defaultIfEmpty(false)
      .repeatWhen { userTokenRepository.onUserTokenChanged() }
}
