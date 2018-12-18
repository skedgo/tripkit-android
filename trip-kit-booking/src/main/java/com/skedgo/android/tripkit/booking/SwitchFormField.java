package com.skedgo.android.tripkit.booking;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class SwitchFormField extends FormField {

  @SerializedName("keyboardType")
  private String keyboardType;
  @SerializedName("value")
  private Boolean value;

  public String getKeyboardType() {
    return keyboardType;
  }

  public void setKeyboardType(String keyboardType) {
    this.keyboardType = keyboardType;
  }

  @Override
  public Object getValue() {
    return value;
  }

  public void setValue(Boolean value) {
    this.value = value;
  }
}