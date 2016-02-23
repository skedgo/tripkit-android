package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

import retrofit.Endpoint;

interface DynamicEndpoint extends Endpoint {
  void setUrl(@NonNull String url);
}