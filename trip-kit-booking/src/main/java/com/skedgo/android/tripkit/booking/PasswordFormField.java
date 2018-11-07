package com.skedgo.android.tripkit.booking;

import android.os.Parcel;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class PasswordFormField extends FormField {

  public static final Creator<PasswordFormField> CREATOR = new Creator<PasswordFormField>() {
    @Override
    public PasswordFormField createFromParcel(Parcel in) {
      in.readInt();
      return new PasswordFormField(in);
    }

    @Override
    public PasswordFormField[] newArray(int size) {
      return new PasswordFormField[size];
    }
  };

  @SerializedName("value")
  private String value;

  public PasswordFormField(Parcel in) {
    super(in);
    this.value = in.readString();
  }

  public PasswordFormField() {
    super();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(PASSWORD);
    super.writeToParcel(dest, flags);
    dest.writeString(value);
  }

  @Nullable
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}

