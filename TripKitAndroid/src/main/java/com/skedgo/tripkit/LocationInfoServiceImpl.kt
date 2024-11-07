package com.skedgo.tripkit;

import com.skedgo.tripkit.common.model.location.Location;
import com.skedgo.tripkit.common.model.region.Region;
import com.skedgo.tripkit.data.regions.RegionService;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.HttpUrl;

final class LocationInfoServiceImpl implements LocationInfoService {
    private final LocationInfoApi api;
    private final RegionService regionService;

    LocationInfoServiceImpl(
        @NonNull LocationInfoApi api,
        @NonNull RegionService regionService) {
        this.api = api;
        this.regionService = regionService;
    }

    @Override
    public Observable<LocationInfo> getLocationInfoAsync(final Location location) {
        return regionService.getRegionByLocationAsync(location)
            .flatMap((Function<Region, Observable<String>>) region -> Observable.fromIterable(region.getURLs()))
            .concatMap((Function<String, Observable<LocationInfo>>) baseUrl -> {
                final String url = HttpUrl.parse(baseUrl).newBuilder()
                    .addPathSegment("locationInfo.json")
                    .build()
                    .toString();
                return api.fetchLocationInfoAsync(
                    url,
                    location.getLat(),
                    location.getLon()
                );
            })
            .filter(response -> response != null).firstElement().toObservable();
    }
}