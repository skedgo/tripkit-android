package com.skedgo.validbookingcount;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersValidBookingCountResponse.class)
public interface ValidBookingCountResponse {
  int count();
}
