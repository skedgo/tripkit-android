package com.skedgo.android.tripkit.booking.model;

import android.os.Parcel;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class StringFormField extends FormField {

  public static final Creator<StringFormField> CREATOR = new Creator<StringFormField>() {
    @Override
    public StringFormField createFromParcel(Parcel in) {
      in.readInt();
      return new StringFormField(in);
    }

    @Override
    public StringFormField[] newArray(int size) {
      return new StringFormField[size];
    }
  };
  @SerializedName("keyboardType")
  private String keyboardType;
  @SerializedName("value")
  private String value;

  public StringFormField(Parcel in) {
    super(in);
    this.keyboardType = in.readString();
    this.value = in.readString();
  }

  public StringFormField() {
    super();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(STRING);
    super.writeToParcel(dest, flags);
    dest.writeString(keyboardType);
    dest.writeString(value);
  }

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
