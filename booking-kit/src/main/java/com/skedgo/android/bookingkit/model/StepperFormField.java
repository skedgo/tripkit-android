package com.skedgo.android.bookingkit.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class StepperFormField extends FormField {
  public static final Creator<StepperFormField> CREATOR = new Creator<StepperFormField>() {
    @Override
    public StepperFormField createFromParcel(Parcel in) {
      in.readInt();
      return new StepperFormField(in);
    }

    @Override
    public StepperFormField[] newArray(int size) {
      return new StepperFormField[size];
    }
  };
  @SerializedName("value")
  private int value;
  @SerializedName("minValue")
  private int minValue;
  @SerializedName("maxValue")
  private int maxValue;

  public StepperFormField() {
    super();
  }

  public StepperFormField(Parcel in) {
    super(in);
    this.value = in.readInt();
    this.minValue = in.readInt();
    this.maxValue = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(STEPPER);
    super.writeToParcel(dest, flags);
    dest.writeInt(value);
    dest.writeInt(minValue);
    dest.writeInt(maxValue);
  }

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