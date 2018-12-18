package com.skedgo.android.tripkit.booking;

import android.os.Parcel;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class PasswordFormField extends FormField {

  @SerializedName("value")
  private String value;

  @Nullable
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
