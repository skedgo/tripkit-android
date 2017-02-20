package com.skedgo.android.tripkit.booking;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class BookingError extends Throwable implements Parcelable {
  public static final Creator<BookingError> CREATOR = new Creator<BookingError>() {
    @Override
    public BookingError createFromParcel(Parcel source) {
      BookingError bookingError = new BookingError();
      bookingError.setTitle(source.readString());
      bookingError.setErrorCode(source.readInt());
      bookingError.setError(source.readString());
      bookingError.setHasUserError(source.readInt() == 1);
      bookingError.setRecovery(source.readString());
      bookingError.setRecoveryTitle(source.readString());
      bookingError.setUrl(source.readString());
      return bookingError;
    }

    @Override
    public BookingError[] newArray(int size) {
      return new BookingError[size];
    }
  };

  @Nullable @SerializedName("title")
  private String title;
  @SerializedName("errorCode")
  private int errorCode;
  @SerializedName("error")
  private String error;
  @SerializedName("usererror")
  private boolean hasUserError;
  @Nullable @SerializedName("recovery")
  private String recovery;
  @Nullable @SerializedName("recoveryTitle")
  private String recoveryTitle;
  @Nullable @SerializedName("url")
  private String url;

  @Nullable
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

  @Nullable public String getRecovery() {
    return recovery;
  }

  public void setRecovery(@Nullable String recovery) {
    this.recovery = recovery;
  }

  @Nullable public String getRecoveryTitle() {
    return recoveryTitle;
  }

  public void setRecoveryTitle(@Nullable String recoveryTitle) {
    this.recoveryTitle = recoveryTitle;
  }

  @Nullable public String getUrl() {
    return url;
  }

  public void setUrl(@Nullable String url) {
    this.url = url;
  }

  public boolean hasUserError() {
    return hasUserError;
  }

  public void setHasUserError(boolean hasUserError) {
    this.hasUserError = hasUserError;
  }

  @Override public String getMessage() {
    if (error != null) {
      return error;
    } else {
      return super.getMessage();
    }
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
    dest.writeInt(hasUserError ? 1 : 0);
    dest.writeString(recovery);
    dest.writeString(recoveryTitle);
    dest.writeString(url);
  }
}
