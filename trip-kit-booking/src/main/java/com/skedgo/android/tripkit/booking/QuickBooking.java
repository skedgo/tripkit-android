package com.skedgo.android.tripkit.booking;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersQuickBooking.class)
public abstract class QuickBooking {

  /**
   * @return URL to book this option.
   * If possible, this will book it without further confirmation.
   */
  @Nullable public abstract String bookingURL();

  /**
   * @return URL to fetch updated trip that's using this booking options.
   * Only present if there would be a change to the trip.
   */
  @Nullable public abstract String tripUpdateURL();

  /**
   * @return Optional URL for image identifying this booking option.
   */
  @Nullable public abstract String imageURL();

  /**
   * @return Localised identifying this booking option.
   */
  @Nullable public abstract String title();

  /**
   * @return Localised description.
   */
  @Nullable public abstract String subtitle();

  /**
   * @return Localised string for doing booking.
   */
  @Nullable public abstract String bookingTitle();

  /**
   * @return Localised human-friendly string, e.g., "$10".
   */
  @Nullable public abstract String priceString();

  /**
   * @return Optional price for this option.
   * If price isn't specified, this will return -1.
   */
  @Value.Default public float price() {
    return -1f;
  }

  /**
   * @return Price in USD dollars.
   * If price isn't specified, this will return -1.
   */
  @Value.Default @SerializedName("USDPrice") public float priceInUSD() {
    return -1f;
  }

  /**
   * @return Optional ETA for this option.
   * This is the expected waiting time.
   */
  @Value.Default @SerializedName("ETA") public float eta() {
    return -1f;
  }
}
