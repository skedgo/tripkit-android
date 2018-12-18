package com.skedgo.android.tripkit.booking;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class BookingError extends Throwable {

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

}
