package skedgo.tripkit.account.domain

import rx.Observable
import javax.inject.Inject

open class GetUserIdentifier @Inject constructor(
    private val userRepository: UserRepository
) {
  open fun execute(): Observable<String>
      = userRepository.getUser()
      .map { "${it.type.hashCode()}${it.name.hashCode()}" }
}
