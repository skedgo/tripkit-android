package skedgo.tripkit.account.domain

import rx.Observable
import rx.Single
import javax.inject.Inject

/**
 * Gets an unique identifier representing an user.
 * The emitted identifier can be used later for [SilentlyLogIn].
 */
open class GetUserIdentifier @Inject constructor(
    private val userRepository: UserRepository,
    private val generateUserKey:GenerateUserKey
) {
  open fun execute(): Observable<String>
      = userRepository.getUserKey().toObservable()
      .flatMap {
        when {
          it.isEmpty() -> {
            generateUserKey.execute().toObservable()
                .flatMap {
                  userRepository.setUserKey(it)
                      .andThen(Single.just(it))
                      .toObservable()
                }
          }
            else -> Observable.just(it)
        }
      }
}
