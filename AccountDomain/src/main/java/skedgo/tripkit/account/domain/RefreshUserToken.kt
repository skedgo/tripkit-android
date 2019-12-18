package skedgo.tripkit.account.domain

import io.reactivex.Observable
import javax.inject.Inject

/**
 * This UseCase is often executed after we change server (e.g. beta, production)
 * and we want to obtain a new [UserToken] that comes from the new server.
 */
open class RefreshUserToken @Inject constructor(
    private val userTokenRepository: UserTokenRepository,
    private val silentlyLogIn: SilentlyLogIn
) {
  open fun execute(): Observable<UserToken>
      = userTokenRepository.clearUserToken()
      .flatMap { silentlyLogIn.execute() }
}
