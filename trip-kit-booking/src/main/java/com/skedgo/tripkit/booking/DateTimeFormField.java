package com.skedgo.tripkit.booking;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class DateTimeFormField extends FormField {

  public static final Creator<DateTimeFormField> CREATOR = new Creator<DateTimeFormField>() {
    @Override
    public DateTimeFormField createFromParcel(Parcel in) {
      in.readInt();
      return new DateTimeFormField(in);
    }

    @Override
    public DateTimeFormField[] newArray(int size) {
      return new DateTimeFormField[size];
    }
  };
  @SerializedName("value")
  private long value;

  public DateTimeFormField() {
    super();
  }

  public DateTimeFormField(Parcel in) {
    super(in);
    value = in.readLong();
  }

  @Override
  public Long getValue() {
    return value;
  }

  public void setValue(long value) {
    this.value = value;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(DATETIME);
    super.writeToParcel(dest, flags);
    dest.writeLong(value);
  }
}
