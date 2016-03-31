package com.skedgo.android.tripkit;

import retrofit2.http.Query;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface LocationInfoApi {

  @GET Observable<LocationInfo> geLocationInfoResponseAsync(
      @Url String url,
      @Query("lat") double lat,
      @Query("lng") double lng
  );
}
