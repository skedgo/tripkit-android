package com.skedgo.tripkit.tsp;

import android.util.Log;
import com.skedgo.android.common.model.Region;

import java.util.List;

import javax.inject.Inject;

import com.skedgo.tripkit.data.tsp.RegionInfo;
import dagger.Lazy;
import io.reactivex.functions.Function;
import okhttp3.HttpUrl;
import io.reactivex.Observable;


import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * A facade of {@link RegionInfoApi} that has failover on multiple servers.
 */
public class RegionInfoService {
  private final Lazy<RegionInfoApi> regionInfoApiLazy;

  @Inject public RegionInfoService(Lazy<RegionInfoApi> regionInfoApiLazy) {
    this.regionInfoApiLazy = regionInfoApiLazy;
  }

  /**
   * @param baseUrls   Can be {@link Region#getURLs()}.
   * @param regionName Can be {@link Region#getName()}.
   */
  public Observable<RegionInfo> fetchRegionInfoAsync(
      List<String> baseUrls,
      final String regionName) {
    return Observable.fromIterable(baseUrls)
        .concatMapDelayError(new Function<String, Observable<RegionInfoResponse>>() {
          @Override public Observable<RegionInfoResponse> apply(final String baseUrl) {
            final String url = HttpUrl.parse(baseUrl).newBuilder()
                .addPathSegment("regionInfo.json")
                .build()
                .toString();
            return regionInfoApiLazy.get().fetchRegionInfoAsync(
                url,
                ImmutableRegionInfoBody.of(regionName)
            );
          }
        })
        .filter(regionInfoResponse -> isNotEmpty(regionInfoResponse.regions()))
        .firstOrError()
        .map(new Function<RegionInfoResponse, RegionInfo>() {
          @Override public RegionInfo apply(RegionInfoResponse response) {
            return response.regions().get(0);
          }
        }).toObservable();
  }
}