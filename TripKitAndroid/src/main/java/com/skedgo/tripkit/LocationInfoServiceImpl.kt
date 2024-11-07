package com.skedgo.tripkit

import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.common.model.region.Region
import com.skedgo.tripkit.data.regions.RegionService
import io.reactivex.Observable
import io.reactivex.functions.Function
import okhttp3.HttpUrl.Companion.toHttpUrl

internal class LocationInfoServiceImpl(
    private val api: LocationInfoApi,
    private val regionService: RegionService
) : LocationInfoService {
    override fun getLocationInfoAsync(location: Location?): Observable<LocationInfo> {
        return regionService.getRegionByLocationAsync(location)
            .flatMap<String?>(Function<Region, Observable<String>> { region: Region ->
                Observable.fromIterable<String>(
                    region.getURLs()
                )
            })
            .concatMap<LocationInfo>(Function<String, Observable<LocationInfo>> { baseUrl: String ->
                val url: String = baseUrl.toHttpUrl().newBuilder()
                    .addPathSegment("locationInfo.json")
                    .build()
                    .toString()
                api.fetchLocationInfoAsync(
                    url,
                    location?.lat,
                    location?.lon
                )
            })
            .filter { response: LocationInfo? -> response != null }.firstElement().toObservable()
    }
}