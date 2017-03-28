package skedgo.tripkit.configuration.domain

import rx.Observable

typealias GetApiKeyHeader = (
    () -> Observable<ApiKey>,
    () -> Observable<ApiKey>
) -> Observable<Pair<String, ApiKey>>

fun getApiKeyHeader(
    getPersistentApiKey: () -> Observable<ApiKey>,
    getDefaultApiKey: () -> Observable<ApiKey>
): Observable<Pair<String, ApiKey>>
    = getPersistentApiKey()
    .switchIfEmpty(getDefaultApiKey())
    .map { Pair("X-TripGo-Key", it) }
