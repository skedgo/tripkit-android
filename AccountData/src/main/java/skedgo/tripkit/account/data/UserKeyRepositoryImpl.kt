package skedgo.tripkit.account.data

import android.content.SharedPreferences
import rx.Completable
import rx.Single
import skedgo.tripkit.account.domain.GenerateUserKey
import skedgo.tripkit.account.domain.UserRepository

internal class UserKeyRepositoryImpl(
    private val prefs: SharedPreferences,
    private val generateUserKey: GenerateUserKey
) : UserRepository {

  private val userKeyKey = "userKey"

  override fun getUserKey(): Single<String> =
      Single.fromCallable {
        prefs.getString(userKeyKey, "")
      }.flatMap { key ->
        when {
          key.isEmpty() ->
            generateUserKey.execute()
                .flatMap { setUserKey(it).andThen(Single.just(it)) }
          else -> Single.just(key)
        }
      }

  private fun setUserKey(userKey: String): Completable =
      Completable.fromCallable {
        prefs.edit()
            .putString(userKey, userKey)
            .apply()
      }
}