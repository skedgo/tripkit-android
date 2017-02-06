package com.skedgo.android.tripkit;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

interface ReportingApi {
  @POST Observable<JsonObject> reportPlannedTripAsync(
      @Body Map<String, Object> userInfo
  );
  @POST Observable<JsonObject> reportPlannedTripAsync(
      @Url String url,
      @Body Map<String, Object> userInfo
  );
}