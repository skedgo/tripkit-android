package com.skedgo.android.tripkit.booking;

import android.os.Parcel;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class StringFormField extends FormField {

  @SerializedName("keyboardType")
  private String keyboardType;
  @SerializedName("value")
  private String value;

  public String getKeyboardType() {
    return keyboardType;
  }

  public void setKeyboardType(String keyboardType) {
    this.keyboardType = keyboardType;
  }

  @Nullable
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
