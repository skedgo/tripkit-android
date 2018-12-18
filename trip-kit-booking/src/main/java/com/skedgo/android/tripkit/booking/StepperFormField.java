package com.skedgo.android.tripkit.booking;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class StepperFormField extends FormField {
  @SerializedName("value")
  private int value;
  @SerializedName("minValue")
  private int minValue;
  @SerializedName("maxValue")
  private int maxValue;

  @Override
  public Integer getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public int getMinValue() {
    return minValue;
  }

  public void setMinValue(int minValue) {
    this.minValue = minValue;
  }

  public int getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(int maxValue) {
    this.maxValue = maxValue;
  }
}