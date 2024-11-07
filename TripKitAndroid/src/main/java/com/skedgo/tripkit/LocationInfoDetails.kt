package com.skedgo.tripkit;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import androidx.annotation.Nullable;

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersLocationInfoDetails.class)
public abstract class LocationInfoDetails {
    @Nullable
    public abstract String w3w();

    @Nullable
    public abstract String w3wInfoURL();
}