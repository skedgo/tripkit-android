package com.skedgo.android.tripkit.booking;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class BookingAction implements Parcelable {
  public static final Creator<BookingAction> CREATOR = new Creator<BookingAction>() {
    @Override
    public BookingAction createFromParcel(Parcel in) {
      return new BookingAction(in);
    }

    @Override
    public BookingAction[] newArray(int size) {
      return new BookingAction[size];
    }
  };
  @SerializedName("title")
  private String title;
  @SerializedName("enabled")
  private boolean enable = true;
  @SerializedName("url")
  private String url;
  @SerializedName("hudText")
  private String hudText;

  public BookingAction() {

  }

  private BookingAction(Parcel in) {
    title = in.readString();
    enable = Boolean.valueOf(in.readString());
    url = in.readString();
    hudText = in.readString();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Nullable
  public String getHudText() {
    return hudText;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(title);
    dest.writeString(String.valueOf(enable));
    dest.writeString(url);
    dest.writeString(hudText);
  }
}
