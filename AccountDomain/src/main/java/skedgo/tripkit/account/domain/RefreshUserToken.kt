package skedgo.tripkit.account.domain

import rx.Observable
import javax.inject.Inject

open class RefreshUserToken @Inject constructor(
    private val userTokenRepository: UserTokenRepository,
    private val getUserIdentifier: GetUserIdentifier
) {
  open fun execute(): Observable<UserToken>
      = userTokenRepository.clearUserToken()
      .flatMap { getUserIdentifier.execute() }
      .flatMap { userTokenRepository.getUserTokenByUserIdentifier(it) }
}
