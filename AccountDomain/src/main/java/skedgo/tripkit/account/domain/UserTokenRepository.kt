package skedgo.tripkit.account.domain

import rx.Observable

interface UserTokenRepository {
  fun getLastKnownUserToken(): Observable<UserToken>
  fun getUserTokenByUserIdentifier(userIdentifier: String): Observable<UserToken>
  fun getUserTokenBySignUpCredentials(signUpCredentials: SignUpCredentials): Observable<UserToken>
  fun getUserTokenBySignInCredentials(signInCredentials: SignInCredentials): Observable<UserToken>
  fun clearUserToken(): Observable<Boolean>
  fun clearUserTokenByLoggingOut(): Observable<Boolean>
  fun onUserTokenChanged(): Observable<Any>
}
