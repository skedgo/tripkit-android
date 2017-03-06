package skedgo.tripkit.account.data

import android.content.SharedPreferences
import rx.Observable
import rx.schedulers.Schedulers
import skedgo.tripkit.account.domain.UserToken
import skedgo.tripkit.account.domain.UserTokenRepository

internal class UserTokenRepositoryImpl constructor(
    private val preferences: SharedPreferences,
    private val silentLoginApi: SilentLoginApi
) : UserTokenRepository {
  companion object {
    val KEY_USER_TOKEN = "userToken"
  }

  override fun getLastKnownUserToken(): Observable<UserToken> {
    return Observable
        .fromCallable {
          preferences.getString(KEY_USER_TOKEN, null)
        }
        .filter { it != null }
        .map(::UserToken)
        .subscribeOn(Schedulers.io())
  }

  override fun getUserTokenByUserIdentifier(userIdentifier: String): Observable<UserToken> {
    return silentLoginApi.logIn("account/android/$userIdentifier")
        .map { UserToken(it.userToken()!!) }
        .doOnNext {
          preferences.edit()
              .putString(KEY_USER_TOKEN, it.value)
              .apply()
        }
  }

  override fun clearUserToken(): Observable<Boolean> = Observable.fromCallable {
    preferences.edit().clear().apply()
    true
  }
}
