package com.skedgo.tripkit.alerts

import io.reactivex.Observable
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import retrofit2.http.Query
import javax.inject.Inject

class RealtimeAlertService @Inject constructor(private val api: RealtimeAlertApi) {

    /**
     * @param baseUrls   Which can be obtained via [Region.getURLs].
     * @param regionName Which can be obtained via [Region.getName].
     */
    fun fetchRealtimeAlertsAsync(
        baseUrls: List<String>?,
        @Query("region") regionName: String
    ): Observable<RealtimeAlertResponse> {
        return if (baseUrls == null) {
            Observable.error(NullPointerException("baseUrls is null"))
        } else {
            Observable.fromIterable(baseUrls)
                .concatMapDelayError { baseUrl ->
                    val url = baseUrl.toHttpUrl()
                        .newBuilder()
                        .addPathSegments("alerts/transit.json")
                        .build()
                        .toString()
                    api.fetchRealtimeAlertsAsync(url, regionName)
                }
                .firstElement()
                .toObservable()
        }
    }
}