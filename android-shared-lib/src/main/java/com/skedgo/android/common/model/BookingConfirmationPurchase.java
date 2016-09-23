package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersBookingConfirmationPurchase.class)
public abstract class BookingConfirmationPurchase implements Parcelable{

  public static final Creator<BookingConfirmationPurchase> CREATOR = new Creator<BookingConfirmationPurchase>() {
    @Override public BookingConfirmationPurchase createFromParcel(Parcel in) {
      return ImmutableBookingConfirmationPurchase.builder()
          .currency(in.readString())
          .id(in.readString())
          .price(in.readString())
          .productName(in.readString())
          .productType(in.readString())
          .build();
    }

    @Override public BookingConfirmationPurchase[] newArray(int size) {
      return new BookingConfirmationPurchase[size];
    }
  };

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(currency());
    dest.writeString(id());
    dest.writeString(price());
    dest.writeString(productName());
    dest.writeString(productType());
  }

  @Override public int describeContents() {
    return 0;
  }

  public abstract String currency();
  public abstract String id();
  public abstract String price();
  public abstract String productName();
  public abstract String productType();
}
