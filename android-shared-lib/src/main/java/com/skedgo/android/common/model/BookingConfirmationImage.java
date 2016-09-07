package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersBookingConfirmationImage.class)
public abstract class BookingConfirmationImage implements Parcelable {

  public static final Creator<BookingConfirmationImage> CREATOR = new Creator<BookingConfirmationImage>() {
    @Override public BookingConfirmationImage createFromParcel(Parcel in) {
      return ImmutableBookingConfirmationImage.builder()
          .imageURL(in.readString())
          .subtitle(in.readString())
          .title(in.readString())
          .build();
    }

    @Override public BookingConfirmationImage[] newArray(int size) {
      return new BookingConfirmationImage[size];
    }
  };

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(imageURL());
    dest.writeString(subtitle());
    dest.writeString(title());
  }

  @Override public int describeContents() {
    return 0;
  }

  public abstract String imageURL();
  public abstract String subtitle();
  public abstract String title();
}
