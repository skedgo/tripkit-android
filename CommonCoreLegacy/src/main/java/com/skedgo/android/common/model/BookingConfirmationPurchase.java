package com.skedgo.android.common.model;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.value.Value;

import androidx.annotation.Nullable;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersBookingConfirmationPurchase.class)
public abstract class BookingConfirmationPurchase {
  public abstract String currency();
  public abstract String id();
  public abstract float price();
  public abstract String productName();
  public abstract String productType();
  @Nullable public abstract String timezone();
  @Nullable public abstract PurchaseBrand brand();
  @Nullable public abstract BookingSource source();

  @Value.Default public long validFor() {
    return 0;
  }

  @Value.Default public long validFrom() {
    return 0;
  }

  @Value.Default public boolean valid() {
    return false;
  }
}