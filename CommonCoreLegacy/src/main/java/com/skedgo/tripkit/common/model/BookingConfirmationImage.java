package com.skedgo.tripkit.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;

import androidx.annotation.Nullable;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
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

  @Nullable public abstract String imageURL();
  public abstract String subtitle();
  public abstract String title();
}
