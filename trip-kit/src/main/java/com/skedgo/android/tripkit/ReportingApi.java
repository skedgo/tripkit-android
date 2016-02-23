package com.skedgo.android.tripkit;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

interface ReportingApi {
  @POST("/") Observable<JsonObject> reportPlannedTripAsync(
      @Body Map<String, Object> userInfo
  );
}