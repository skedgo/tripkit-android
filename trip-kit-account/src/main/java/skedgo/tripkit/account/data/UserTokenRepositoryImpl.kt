package skedgo.tripkit.account.data

import android.content.SharedPreferences
import com.skedgo.android.tripkit.account.LogInBody
import com.skedgo.android.tripkit.account.SignUpBody
import rx.Emitter
import rx.Observable
import rx.schedulers.Schedulers
import skedgo.tripkit.account.domain.SignInCredentials
import skedgo.tripkit.account.domain.SignUpCredentials
import skedgo.tripkit.account.domain.UserToken
import skedgo.tripkit.account.domain.UserTokenRepository

internal class UserTokenRepositoryImpl constructor(
    private val preferences: SharedPreferences,
    private val silentLoginApi: SilentLoginApi,
    private val accountApi: AccountApi
) : UserTokenRepository {
  companion object {
    val KEY_USER_TOKEN = "userToken"
  }

  override fun clearUserTokenByLoggingOut(): Observable<Boolean>
      = accountApi.logOut().flatMap { clearUserToken() }

  override fun getUserTokenBySignInCredentials(
      signInCredentials: SignInCredentials): Observable<UserToken>
      = Observable.just(signInCredentials)
      .map {
        LogInBody.builder()
            .username(it.email)
            .password(it.password)
            .build()
      }
      .flatMap { accountApi.logIn(it) }
      .map { it.userToken() }
      .map { UserToken(it!!) }
      .doOnNext {
        preferences.edit()
            .putString(KEY_USER_TOKEN, it.value)
            .apply()
      }

  override fun onUserTokenChanged(): Observable<Any>
      = preferences.onChange(KEY_USER_TOKEN).map { this }

  override fun getUserTokenBySignUpCredentials(
      signUpCredentials: SignUpCredentials): Observable<UserToken>
      = Observable.just(signUpCredentials)
      .map {
        SignUpBody.builder()
            .username(it.email)
            .password(it.password)
            .build()
      }
      .flatMap { accountApi.signUp(it) }
      .map { it.userToken() }
      .map { UserToken(it!!) }
      .doOnNext {
        preferences.edit()
            .putString(KEY_USER_TOKEN, it.value)
            .apply()
      }

  override fun getLastKnownUserToken(): Observable<UserToken>
      = Observable.fromCallable { preferences.getString(KEY_USER_TOKEN, null) }
      .filter { it != null }
      .map(::UserToken)
      .subscribeOn(Schedulers.io())

  override fun getUserTokenByUserIdentifier(userIdentifier: String): Observable<UserToken>
      = silentLoginApi.logIn("account/android/$userIdentifier")
      .map { UserToken(it.userToken()!!) }
      .doOnNext {
        preferences.edit()
            .putString(KEY_USER_TOKEN, it.value)
            .apply()
      }

  override fun clearUserToken(): Observable<Boolean> = Observable.fromCallable {
    preferences.edit().clear().apply()
    true
  }

  internal fun SharedPreferences.onChange(observedKey: String)
      = Observable
      .fromEmitter<String>({
        val listener = SharedPreferences.OnSharedPreferenceChangeListener {
          preferences, key ->
          it.onNext(key)
        }
        registerOnSharedPreferenceChangeListener(listener)
        it.setCancellation { unregisterOnSharedPreferenceChangeListener(listener) }
      }, Emitter.BackpressureMode.BUFFER)
      .filter { it == observedKey }
}
