package com.skedgo.tripkit.data.database.locations.carparks;

import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@Immutable
@TypeAdapters
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersGroup.class)
interface Group {
  @Nullable CarParkLocation[] carParks();
}