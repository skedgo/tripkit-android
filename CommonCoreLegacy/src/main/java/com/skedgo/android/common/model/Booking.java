package com.skedgo.android.common.model;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

import androidx.annotation.Nullable;

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersBooking.class)
public abstract class Booking {
  @SerializedName("externalActions") @Nullable public abstract List<String> getExternalActions();
  @SerializedName("title") @Nullable public abstract String getTitle();
  @SerializedName("url") @Nullable public abstract String getUrl();
  @SerializedName("quickBookingsUrl") @Nullable public abstract String getQuickBookingsUrl();
  @SerializedName("confirmation") @Nullable public abstract BookingConfirmation getConfirmation();
}