package com.skedgo.tripkit;

import com.google.gson.annotations.SerializedName;
import com.skedgo.tripkit.common.model.region.RegionsResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface RegionsApi {
    @POST("regions.json")
    Single<RegionsResponse> fetchRegionsAsyncAsSingle(@Body RequestBodyContent bodyContent);

    @POST("regions.json")
    Observable<RegionsResponse> fetchRegionsAsync(@Body RequestBodyContent bodyContent);

    final class RequestBodyContent {
        @SerializedName("v")
        private int apiVersion;
        @SerializedName("app")
        private String appFlavor;

        public RequestBodyContent(int apiVersion, String appFlavor) {
            this.apiVersion = apiVersion;
            this.appFlavor = appFlavor;
        }
    }
}