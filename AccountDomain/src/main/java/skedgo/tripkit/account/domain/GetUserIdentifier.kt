package skedgo.tripkit.account.domain

import rx.Observable
import javax.inject.Inject

/**
 * Gets an unique identifier representing an user.
 * The emitted identifier can be used later for [SilentlyLogIn].
 */
open class GetUserIdentifier @Inject constructor(
    private val userRepository: UserRepository
) {
  open fun execute(): Observable<String>
      = userRepository.getUser()
      .map { "${it.type.hashCode()}${it.name.hashCode()}" }
}
