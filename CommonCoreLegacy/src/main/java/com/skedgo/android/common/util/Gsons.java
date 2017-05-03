package com.skedgo.android.common.util;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.android.common.model.GsonAdaptersBooking;
import com.skedgo.android.common.model.GsonAdaptersRealtimeAlert;
import com.skedgo.android.common.model.Region;

public final class Gsons {
  private Gsons() {}

  @NonNull
  public static Gson createForLowercaseEnum() {
    return new GsonBuilder()
        .registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
        .registerTypeAdapterFactory(new GsonAdaptersBooking())
        .registerTypeAdapterFactory(new GsonAdaptersRealtimeAlert())
        .create();
  }

  @NonNull
  public static Gson createForRegion() {
    return new GsonBuilder()
        .registerTypeAdapter(Region.City.class, new CityJsonDeserializer())
        .create();
  }
}