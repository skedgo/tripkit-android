package skedgo.tripkit.account.domain

import rx.Observable
import javax.inject.Inject

open class GetUserTokenHeaderValue @Inject constructor(
    private val userTokenRepository: UserTokenRepository
) {
  open fun execute(): Observable<String?>
      = userTokenRepository.getLastKnownUserToken()
      .map { it.value }
      .defaultIfEmpty(null)
}
