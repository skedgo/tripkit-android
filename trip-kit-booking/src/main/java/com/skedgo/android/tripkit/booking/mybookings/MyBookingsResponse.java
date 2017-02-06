package com.skedgo.android.tripkit.booking.mybookings;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersMyBookingsResponse.class)
public interface MyBookingsResponse {
  List<MyBookingsConfirmationResponse> bookings();
}
