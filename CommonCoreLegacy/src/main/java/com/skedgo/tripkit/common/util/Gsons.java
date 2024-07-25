package com.skedgo.tripkit.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.tripkit.common.model.GsonAdaptersBooking;
import com.skedgo.tripkit.common.model.GsonAdaptersRealtimeAlert;
import com.skedgo.tripkit.common.model.Region;

import androidx.annotation.NonNull;

public final class Gsons {
    private Gsons() {
    }

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