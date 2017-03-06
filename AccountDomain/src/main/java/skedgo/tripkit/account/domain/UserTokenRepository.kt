package skedgo.tripkit.account.domain

import rx.Observable

interface UserTokenRepository {
  fun getLastKnownUserToken(): Observable<UserToken>
  fun getUserTokenByUserIdentifier(userIdentifier: String): Observable<UserToken>
  fun clearUserToken(): Observable<Boolean>
}
