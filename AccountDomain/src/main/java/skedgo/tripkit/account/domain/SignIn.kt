package skedgo.tripkit.account.domain

import rx.Observable
import javax.inject.Inject

open class SignIn @Inject constructor(
    private val userTokenRepository: UserTokenRepository
) {
  open fun execute(signInCredentials: SignInCredentials): Observable<UserToken> {
    return userTokenRepository.getUserTokenBySignInCredentials(signInCredentials)
  }
}
