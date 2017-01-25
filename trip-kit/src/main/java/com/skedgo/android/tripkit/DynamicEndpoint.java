package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

import retrofit2.http.Url;

interface DynamicEndpoint {
  void setUrl(@Url @NonNull String url);
  String getUrl();
}