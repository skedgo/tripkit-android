package com.skedgo.tripkit.alerts;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.tripkit.routing.ModeInfo;

import org.immutables.value.Value;

import androidx.annotation.Nullable;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@Immutable
@TypeAdapters
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersRoute.class)
public abstract class Route {
    public abstract String id();

    @Nullable
    public abstract String name();

    @Nullable
    public abstract String number();

    @Nullable
    public abstract ModeInfo modeInfo();

    @Value.Default
    int type() {
        return -1;
    }
}
