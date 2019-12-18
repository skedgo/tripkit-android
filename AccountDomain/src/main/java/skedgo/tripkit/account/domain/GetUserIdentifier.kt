package skedgo.tripkit.account.domain

import io.reactivex.Observable
import javax.inject.Inject

/**
 * Gets an unique identifier representing an user.
 * The emitted identifier can be used later for [SilentlyLogIn].
 */
open class GetUserIdentifier @Inject constructor(
    private val userKeyRepository: UserKeyRepository
) {
  open fun execute(): Observable<String> = userKeyRepository.getUserKey().toObservable()
}
