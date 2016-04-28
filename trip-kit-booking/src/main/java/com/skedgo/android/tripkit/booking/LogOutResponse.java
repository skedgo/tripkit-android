package com.skedgo.android.tripkit.booking;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersLogOutResponse.class)
public abstract class LogOutResponse {
  /**
   * Indicates whether user data was removed or not.
   */
  @Value.Default public boolean changed() {
    return false;
  }
}