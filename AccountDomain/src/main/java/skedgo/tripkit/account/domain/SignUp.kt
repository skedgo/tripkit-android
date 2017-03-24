package skedgo.tripkit.account.domain

import rx.Observable
import javax.inject.Inject

open class SignUp @Inject constructor(
    private val userTokenRepository: UserTokenRepository
) {
  open fun execute(signUpCredentials: SignUpCredentials): Observable<UserToken>
      = userTokenRepository.getUserTokenBySignUpCredentials(signUpCredentials)
}
