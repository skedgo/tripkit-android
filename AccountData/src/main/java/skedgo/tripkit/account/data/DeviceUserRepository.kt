package skedgo.tripkit.account.data

import android.accounts.AccountManager
import rx.Observable
import rx.schedulers.Schedulers
import skedgo.tripkit.account.domain.User
import skedgo.tripkit.account.domain.UserRepository

internal class DeviceUserRepository constructor(
    private val accountManager: AccountManager
) : UserRepository {
  override fun getUser(): Observable<User> {
    return Observable
        .fromCallable {
          accountManager.getAccountsByType("com.google").first()
        }
        .map { User(it.name, it.type) }
        .subscribeOn(Schedulers.io())
  }
}
