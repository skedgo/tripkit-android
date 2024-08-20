package com.skedgo.tripkit.account.domain

import io.reactivex.Observable
import javax.inject.Inject

open class SilentlyLogIn @Inject constructor(
    private val userTokenRepository: UserTokenRepository,
    private val getUserIdentifier: GetUserIdentifier
) {
    open fun execute(): Observable<UserToken> = userTokenRepository.getLastKnownUserToken()
        .switchIfEmpty(
            Observable.defer {
                getUserIdentifier.execute()
            }
                .flatMap { userTokenRepository.getUserTokenByUserIdentifier(it) }
        )
}
