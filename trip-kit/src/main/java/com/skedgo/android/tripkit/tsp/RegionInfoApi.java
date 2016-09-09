package com.skedgo.android.tripkit.tsp;

import com.skedgo.android.common.model.Region;
import com.skedgo.android.tripkit.RegionInfoResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Retrieves detailed information about covered
 * transport service providers for the specified regions.
 * <p>
 * See http://skedgo.github.io/tripgo-api/swagger/#!/Configuration/post_regionInfo_json.
 * See {@link RegionInfoService} for easier usage.
 */
public interface RegionInfoApi {
  @POST("regionInfo.json") RegionInfoResponse fetchRegionInfo(
      @Body RegionInfoBody body
  );

  /**
   * @param url The url is a composition of an URL
   *            from {@link Region#getURLs()} and 'regionInfo.json'.
   */
  @POST Observable<RegionInfoResponse> fetchRegionInfoAsync(
      @Url String url,
      @Body RegionInfoBody body
  );
}