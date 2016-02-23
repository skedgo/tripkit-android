package com.skedgo.android.tripkit;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.POST;

interface RegionInfoApi {
  @POST("/regionInfo.json") Response fetchRegionInfo(@Body RequestBody body);

  final class RequestBody {
    @SerializedName("region") private String region;

    public RequestBody(String region) {
      this.region = region;
    }
  }

  class Response {
    @SerializedName("regions") private List<RegionInfo> regions;

    public Response() {}

    public Response(List<RegionInfo> regions) {
      this.regions = regions;
    }

    @Nullable public List<RegionInfo> regions() {
      return regions;
    }

    static class RegionInfo {
      @SerializedName("paratransit") private Paratransit paratransit;

      public RegionInfo() {}

      public RegionInfo(Paratransit paratransit) {
        this.paratransit = paratransit;
      }

      @Nullable public Paratransit paratransit() {
        return paratransit;
      }
    }
  }
}