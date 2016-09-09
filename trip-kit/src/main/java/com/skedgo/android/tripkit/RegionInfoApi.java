package com.skedgo.android.tripkit;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Retrieves detailed information about covered
 * transport service providers for the specified regions.
 * <p>
 * See http://skedgo.github.io/tripgo-api/swagger/#!/Configuration/post_regionInfo_json.
 */
public interface RegionInfoApi {
  @POST("regionInfo.json") RegionInfoResponse fetchRegionInfo(
      @Body RegionInfoBody body
  );

  @POST Observable<RegionInfoResponse> fetchRegionInfoAsync(
      @Url String url,
      @Body RegionInfoBody body
  );
}