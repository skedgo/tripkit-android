package com.skedgo.android.tripkit.booking;

import android.os.Parcel;

public class PasswordFormField extends StringFormField {

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

  public PasswordFormField(Parcel in) {
    super(in);
  }

  public PasswordFormField() {
    super();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(PASSWORD);
    super.writeToParcel(dest, flags);
  }

  public String getKeyboardType() {
    return null;
  }

  public void setKeyboardType(String keyboardType) {}

}

