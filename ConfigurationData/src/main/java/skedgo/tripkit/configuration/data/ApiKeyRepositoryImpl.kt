package skedgo.tripkit.configuration.data

import android.content.SharedPreferences
import rx.Observable
import rx.schedulers.Schedulers
import skedgo.tripkit.configuration.domain.ApiKey
import skedgo.tripkit.configuration.domain.ApiKeyRepository

internal class ApiKeyRepositoryImpl constructor(
    private val prefs: SharedPreferences
) : ApiKeyRepository {
  val apiKeyKey = "apiKey"

  override fun getApiKey(): Observable<ApiKey>
      = Observable.fromCallable { prefs.getString(apiKeyKey, null) }
      .filter { it != null }
      .map(::ApiKey)
      .subscribeOn(Schedulers.io())

  override fun putApiKey(apiKey: ApiKey): Observable<Unit> = Observable
      .fromCallable {
        prefs.edit()
            .putString(apiKeyKey, apiKey.value)
            .apply()
      }
}
