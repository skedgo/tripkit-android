package com.skedgo.android.tripkit.tsp;

import com.skedgo.android.common.model.Region;
import com.skedgo.android.tripkit.RegionInfo;
import com.skedgo.android.tripkit.RegionInfoResponse;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import okhttp3.HttpUrl;
import rx.Observable;
import rx.functions.Func1;

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
    return Observable.from(baseUrls)
        .concatMapDelayError(new Func1<String, Observable<RegionInfoResponse>>() {
          @Override public Observable<RegionInfoResponse> call(final String baseUrl) {
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
        .first(new Func1<RegionInfoResponse, Boolean>() {
          @Override public Boolean call(RegionInfoResponse response) {
            return isNotEmpty(response.regions());
          }
        })
        .map(new Func1<RegionInfoResponse, RegionInfo>() {
          @Override public RegionInfo call(RegionInfoResponse response) {
            return response.regions().get(0);
          }
        });
  }
}