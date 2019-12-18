package com.skedgo.tripkit;

import com.google.gson.annotations.SerializedName;
import com.skedgo.tripkit.common.model.RegionsResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import io.reactivex.Observable;

interface RegionsApi {
  @POST("regions.json")
  Observable<RegionsResponse> fetchRegionsAsync(@Body RequestBodyContent bodyContent);

  final class RequestBodyContent {
    @SerializedName("v") private int apiVersion;
    @SerializedName("app") private String appFlavor;

    public RequestBodyContent(int apiVersion, String appFlavor) {
      this.apiVersion = apiVersion;
      this.appFlavor = appFlavor;
    }
  }
}