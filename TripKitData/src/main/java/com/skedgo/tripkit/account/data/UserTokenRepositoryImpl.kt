package com.skedgo.tripkit.account.data

import android.content.SharedPreferences
import com.skedgo.tripkit.account.domain.SignInCredentials
import com.skedgo.tripkit.account.domain.SignUpCredentials
import com.skedgo.tripkit.account.domain.UserToken
import com.skedgo.tripkit.account.domain.UserTokenRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

internal class UserTokenRepositoryImpl constructor(
    private val preferences: SharedPreferences,
    private val silentLoginApi: SilentLoginApi,
    private val accountApi: AccountApi
) : UserTokenRepository {
  companion object {
    const val KEY_USER_TOKEN = "userToken"
  }

  override fun clearUserTokenByLoggingOut(): Observable<Boolean> =
      accountApi.logOut().flatMap { clearUserToken() }

  override fun getUserTokenBySignInCredentials(
      signInCredentials: SignInCredentials
  ): Observable<UserToken> =
      Observable.just(signInCredentials)
          .map {
            ImmutableLogInBody.builder()
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

    override fun onUserTokenChanged(): Observable<Any> =
      preferences.onChange(KEY_USER_TOKEN).cast(Any::class.java)

  override fun getUserTokenBySignUpCredentials(
      signUpCredentials: SignUpCredentials
  ): Observable<UserToken> =
      Observable.just(signUpCredentials)
          .map {
            ImmutableSignUpBody.builder()
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

  override fun getLastKnownUserToken(): Observable<UserToken> =
      Observable.fromCallable {
         preferences.getString(KEY_USER_TOKEN, "") ?: "" }
          .filter { it.isNotBlank() }
          .map(::UserToken)
          .subscribeOn(Schedulers.io())

  override fun getUserTokenByUserIdentifier(userIdentifier: String): Observable<UserToken> =
      silentLoginApi.logIn("account/android/$userIdentifier")
          .map { UserToken(it.userToken()!!) }
          .doOnNext {
            preferences.edit()
                .putString(KEY_USER_TOKEN, it.value)
                .apply()
          }

    override fun setUserToken(userToken: String) {
        preferences.edit()
                .putString(KEY_USER_TOKEN, userToken)
                .apply()

    }


    override fun clearUserToken(): Observable<Boolean> = Observable.fromCallable {
    preferences.edit()
        .putString(KEY_USER_TOKEN, null)
        .apply()
    true
  }

  override fun hasUserToken(): Boolean = preferences.getString(KEY_USER_TOKEN, "") != ""

  private fun SharedPreferences.onChange(observedKey: String) = Flowable
      .create<String>({
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
          it.onNext(key)
        }
        registerOnSharedPreferenceChangeListener(listener)
        it.setCancellable { unregisterOnSharedPreferenceChangeListener(listener) }
      }, BackpressureStrategy.BUFFER)
      .filter { it == observedKey }.toObservable()
}
