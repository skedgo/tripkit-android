package com.skedgo.android.bookingkit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BookingError implements Parcelable {
  public static final Creator<BookingError> CREATOR = new Creator<BookingError>() {
    @Override
    public BookingError createFromParcel(Parcel source) {
      BookingError bookingError = new BookingError();
      bookingError.setTitle(source.readString());
      bookingError.setErrorCode(source.readInt());
      bookingError.setError(source.readString());
      return bookingError;
    }

    @Override
    public BookingError[] newArray(int size) {
      return new BookingError[size];
    }
  };

  @SerializedName("title")
  private String title;
  @SerializedName("errorCode")
  private int errorCode;
  @SerializedName("error")
  private String error;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(title);
    dest.writeInt(errorCode);
    dest.writeString(error);
  }
}
