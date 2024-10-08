package com.skedgo.tripkit;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.tripkit.common.model.stop.ScheduledStop;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import androidx.annotation.Nullable;

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersLocationInfo.class)
public interface LocationInfo {
    @Nullable
    LocationInfoDetails details();

    @Nullable
    ScheduledStop stop();

    @Nullable
    CarPark carPark();

    double lat();

    double lng();
}