package skedgo.tripkit.account.data

import android.content.SharedPreferences
import rx.Completable
import rx.Single
import skedgo.tripkit.account.domain.GenerateUserKey
import skedgo.tripkit.account.domain.UserKeyRepository

internal class UserKeyRepositoryImpl(
    private val prefs: SharedPreferences,
    private val generateUserKey: GenerateUserKey
) : UserKeyRepository {

  private val KEY_USER_UUID = "userUUID"

  override fun getUserKey(): Single<String> =
      Single.fromCallable {
        prefs.getString(KEY_USER_UUID, null)
      }.flatMap { key ->
        when {
          key.isNullOrEmpty() ->
            generateUserKey.execute()
                .flatMap { setUserKey(it).andThen(Single.just(it)) }
          else -> Single.just(key)
        }
      }

  private fun setUserKey(userKey: String): Completable =
      Completable.fromCallable {
        prefs.edit()
            .putString(KEY_USER_UUID, userKey)
            .apply()
      }
}