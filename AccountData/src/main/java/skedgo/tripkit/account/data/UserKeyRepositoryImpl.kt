package skedgo.tripkit.account.data

import android.content.SharedPreferences
import rx.Completable
import rx.Single
import skedgo.tripkit.account.domain.UserRepository

internal class UserKeyRepositoryImpl(
    private val prefs: SharedPreferences
) : UserRepository {

  private val userKeyKey = "userKey"

  override fun getUserKey(): Single<String> =
      Single.fromCallable {
        prefs.getString(userKeyKey, "")
      }

  override fun setUserKey(userKey: String): Completable =
      Completable.fromCallable {
        prefs.edit()
            .putString(userKey, userKey)
            .apply()
      }.doOnEach {  }
}