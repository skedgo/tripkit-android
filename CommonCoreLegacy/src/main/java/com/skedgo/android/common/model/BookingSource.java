package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersBookingSource.class)
public abstract class BookingSource implements Parcelable {

  public static final Creator<BookingSource> CREATOR = new Creator<BookingSource>() {
    @Override public BookingSource createFromParcel(Parcel in) {
      return ImmutableBookingSource.builder()
          .provider((BookingProvider) in.readParcelable(BookingProvider.class.getClassLoader()))
          .build();
    }

    @Override public BookingSource[] newArray(int size) {
      return new BookingSource[size];
    }
  };

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(provider(), flags);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Nullable public abstract BookingProvider provider();

}
