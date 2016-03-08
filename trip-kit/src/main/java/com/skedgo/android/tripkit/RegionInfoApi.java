package com.skedgo.android.tripkit;

import com.google.gson.annotations.SerializedName;

import retrofit.http.Body;
import retrofit.http.POST;

interface RegionInfoApi {
  @POST("/regionInfo.json") RegionInfoResponse fetchRegionInfo(@Body RequestBody body);

  final class RequestBody {
    @SerializedName("region") private String region;

    public RequestBody(String region) {
      this.region = region;
    }
  }
}