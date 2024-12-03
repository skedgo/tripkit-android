package com.skedgo.tripkit.tsp

import com.skedgo.tripkit.data.tsp.RegionInfo
import dagger.Lazy
import io.reactivex.Observable
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.apache.commons.collections4.CollectionUtils
import javax.inject.Inject

/**
 * A facade of [RegionInfoApi] that has failover on multiple servers.
 */
class RegionInfoService @Inject constructor(
    private val regionInfoApiLazy: Lazy<RegionInfoApi>
) {

    /**
     * @param baseUrls   Can be {@link Region#getURLs()}.
     * @param regionName Can be {@link Region#getName()}.
     */
    fun fetchRegionInfoAsync(
        baseUrls: List<String>,
        regionName: String?
    ): Observable<RegionInfo> {
        return Observable.fromIterable(baseUrls)
            .concatMapDelayError { baseUrl ->
                val url = baseUrl.toHttpUrlOrNull()?.newBuilder()
                    ?.addPathSegment("regionInfo.json")
                    ?.build()
                    .toString()
                regionInfoApiLazy.get().fetchRegionInfoAsync(
                    url,
                    ImmutableRegionInfoBody.of(regionName)
                ).doOnError { it.printStackTrace() }
            }
            .filter { regionInfoResponse -> regionInfoResponse.regions().isNotEmpty() }
            .firstOrError()
            .map { response -> response.regions()[0] }
            .toObservable()
    }
}