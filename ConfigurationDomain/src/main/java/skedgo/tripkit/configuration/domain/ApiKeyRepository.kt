package skedgo.tripkit.configuration.domain

import rx.Observable

interface ApiKeyRepository {
  fun getApiKey(): Observable<ApiKey>
  fun putApiKey(apiKey: ApiKey): Observable<Unit>
}
