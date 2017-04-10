package com.skedgo.android.tripkit;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

interface LocationInfoApi {
  @GET Observable<LocationInfo> fetchLocationInfoAsync(
      @Url String url,
      @Query("lat") double lat,
      @Query("lng") double lng
  );
}