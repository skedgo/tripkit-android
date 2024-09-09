package com.skedgo.tripkit.account.domain

import io.reactivex.Observable
import javax.inject.Inject

open class SignIn @Inject constructor(
    private val userTokenRepository: UserTokenRepository
) {
    open fun execute(signInCredentials: SignInCredentials): Observable<UserToken> =
        userTokenRepository.getUserTokenBySignInCredentials(signInCredentials)
}
