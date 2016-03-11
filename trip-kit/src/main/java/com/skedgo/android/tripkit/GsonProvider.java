package com.skedgo.android.tripkit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.android.common.util.LowercaseEnumTypeAdapterFactory;

final class GsonProvider {
  private static Gson gson;

  public static synchronized Gson get() {
    if (gson == null) {
      gson = new GsonBuilder()
          .registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
          .registerTypeAdapterFactory(new GsonAdaptersRegionInfo())
          .registerTypeAdapterFactory(new GsonAdaptersRegionInfoResponse())
          .create();
    }

    return gson;
  }
}