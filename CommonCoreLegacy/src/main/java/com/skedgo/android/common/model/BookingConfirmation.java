package com.skedgo.android.common.model;

import com.google.gson.annotations.JsonAdapter;

import java.util.List;

import androidx.annotation.Nullable;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersBookingConfirmation.class)
public abstract class BookingConfirmation {

  public abstract List<BookingConfirmationAction> actions();
  @Nullable public abstract BookingConfirmationImage provider();
  @Nullable public abstract BookingConfirmationPurchase purchase();
  public abstract BookingConfirmationStatus status();
  @Nullable public abstract BookingConfirmationImage vehicle();

}