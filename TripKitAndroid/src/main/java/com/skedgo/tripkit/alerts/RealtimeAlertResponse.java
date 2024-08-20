package com.skedgo.tripkit.alerts;

import com.google.gson.annotations.JsonAdapter;

import java.util.List;

import androidx.annotation.Nullable;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@Immutable
@TypeAdapters
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersRealtimeAlertResponse.class)
public interface RealtimeAlertResponse {
    @Nullable
    List<AlertBlock> alerts();
}