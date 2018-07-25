package skedgo.tripkit.account.data

import android.app.backup.BackupManager
import android.content.SharedPreferences
import rx.Completable
import rx.Single
import skedgo.tripkit.account.domain.UserKeyRepository
import java.util.*


internal class UserKeyRepositoryImpl(
    private val prefs: SharedPreferences,
    private val backupManager: BackupManager
) : UserKeyRepository {

  private val KEY_USER_UUID = "userUUID"

  override fun getUserKey(): Single<String> =
      Single.fromCallable {
        prefs.getString(KEY_USER_UUID, null)
      }.flatMap { key ->
        when {
          key.isNullOrEmpty() ->
            UUID.randomUUID().toString().let {
              setUserKey(it).andThen(Single.just(it))
            }
          else -> Single.just(key)
        }
      }

  private fun setUserKey(userKey: String): Completable =
      Completable.fromCallable {
        prefs.edit()
            .putString(KEY_USER_UUID, userKey)
            .apply()
      }
          .andThen(Completable.fromCallable {
            backupManager.dataChanged()
          })
}