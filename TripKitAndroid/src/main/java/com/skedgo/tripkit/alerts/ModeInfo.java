package com.skedgo.tripkit.alerts;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.tripkit.routing.ServiceColor;

import androidx.annotation.Nullable;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@Immutable
@TypeAdapters
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersModeInfo.class)
public interface ModeInfo {
    @Nullable
    ServiceColor color();

    @Nullable
    String identifier();

    @Nullable
    String alt();
}