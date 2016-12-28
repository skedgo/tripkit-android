package com.skedgo.android.tripkit.booking.mybookings;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.android.common.model.BookingConfirmation;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersMyBookingsResponse.class)
public interface MyBookingsResponse {
  List<BookingConfirmation> bookings();
}
