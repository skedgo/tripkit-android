package com.skedgo.android.tripkit.booking;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class SwitchFormField extends FormField {

  public static final Creator<SwitchFormField> CREATOR = new Creator<SwitchFormField>() {
    @Override
    public SwitchFormField createFromParcel(Parcel in) {
      in.readInt();
      return new SwitchFormField(in);
    }

    @Override
    public SwitchFormField[] newArray(int size) {
      return new SwitchFormField[size];
    }
  };
  @SerializedName("keyboardType")
  private String keyboardType;
  @SerializedName("value")
  private Boolean value;

  public SwitchFormField(Parcel in) {
    super(in);
    this.keyboardType = in.readString();
    this.value = Boolean.valueOf(in.readString());
  }

  public SwitchFormField() {
    super();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(SWITCH);
    super.writeToParcel(dest, flags);
    dest.writeString(keyboardType);
    dest.writeString(value.toString());
  }

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
