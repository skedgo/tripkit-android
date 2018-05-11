package com.skedgo.android.tripkit.booking.mybookings;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@Immutable
@TypeAdapters
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersMyBookingsResponse.class)
public abstract class MyBookingsResponse {
  public abstract List<MyBookingsConfirmationResponse> bookings();

  @Value.Default public int count() {
    return 0;
  }
}
