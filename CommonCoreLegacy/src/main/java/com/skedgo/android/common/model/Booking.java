package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.ArrayList;
import java.util.List;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Booking implements Parcelable {
  public static final Creator<Booking> CREATOR = new Creator<Booking>() {
    @SuppressWarnings("unchecked")
    @Override public Booking createFromParcel(Parcel in) {
      final ArrayList<String> externalActions = in.readArrayList(ArrayList.class.getClassLoader());
      return ImmutableBooking.builder()
          .externalActions(externalActions)
          .title(in.readString())
          .url(in.readString())
          .quickBookingsUrl(in.readString())
          .confirmation((BookingConfirmation) in.readParcelable(BookingConfirmation.class.getClassLoader()))
          .build();
    }

    @Override public Booking[] newArray(int size) {
      return new Booking[size];
    }
  };

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeList(getExternalActions());
    dest.writeString(getTitle());
    dest.writeString(getUrl());
    dest.writeString(getQuickBookingsUrl());
    dest.writeParcelable(getConfirmation(), flags);

  }

  @Override public int describeContents() {
    return 0;
  }

  @SerializedName("externalActions") @Nullable public abstract List<String> getExternalActions();
  @SerializedName("title") @Nullable public abstract String getTitle();
  @SerializedName("url") @Nullable public abstract String getUrl();
  @SerializedName("quickBookingsUrl") @Nullable public abstract String getQuickBookingsUrl();
  @SerializedName("confirmation") @Nullable public abstract BookingConfirmation getConfirmation();
}