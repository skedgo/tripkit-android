package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersBookingConfirmationAction.class)
public abstract class BookingConfirmationAction {
  public static final String TYPE_CANCEL = "CANCEL";
  public static final String TYPE_CALL = "CALL";
  public static final String TYPE_QR_CODE = "QRCODE";

  @Nullable public abstract String internalURL();
  @Nullable public abstract String externalURL();
  public abstract boolean isDestructive();
  public abstract String title();
  public abstract String type();
}

