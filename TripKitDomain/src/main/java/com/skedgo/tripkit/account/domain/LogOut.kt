package com.skedgo.tripkit.account.domain

import io.reactivex.Observable
import javax.inject.Inject

open class LogOut @Inject constructor(
    private val userTokenRepository: UserTokenRepository
) {
    open fun execute(): Observable<Boolean> = userTokenRepository.clearUserTokenByLoggingOut()
}
