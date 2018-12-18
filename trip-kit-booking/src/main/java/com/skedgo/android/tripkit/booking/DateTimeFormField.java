package com.skedgo.android.tripkit.booking;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class DateTimeFormField extends FormField {
  @SerializedName("value")
  private long value;

  @Override
  public Long getValue() {
    return value;
  }

  public void setValue(long value) {
    this.value = value;
  }
}