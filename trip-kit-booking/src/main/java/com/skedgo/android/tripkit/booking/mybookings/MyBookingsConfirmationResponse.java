package com.skedgo.android.tripkit.booking.mybookings;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.android.common.model.BookingConfirmation;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersMyBookingsConfirmationResponse.class)
public abstract class MyBookingsConfirmationResponse {

  public abstract BookingConfirmation confirmation();

  @Value.Default public int count() {
    return 0;
  }

  @Value.Default public int index() {
    return 0;
  }

  @Value.Default public int time() {
    return 0;
  }

  public abstract @Nullable List<String> trips();

}
