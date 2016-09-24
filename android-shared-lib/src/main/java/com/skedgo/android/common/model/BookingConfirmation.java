package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.ArrayList;
import java.util.List;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersBookingConfirmation.class)
public abstract class BookingConfirmation implements Parcelable {

  public static final Creator<BookingConfirmation> CREATOR = new Creator<BookingConfirmation>() {
    @Override public BookingConfirmation createFromParcel(Parcel in) {

      List<BookingConfirmationAction> actions = new ArrayList<>();
      in.readTypedList(actions, BookingConfirmationAction.CREATOR);

      return ImmutableBookingConfirmation.builder()
          .actions(actions)
          .provider((BookingConfirmationImage) in.readParcelable(BookingConfirmationImage.class.getClassLoader()))
          .purchase((BookingConfirmationPurchase) in.readParcelable(BookingConfirmationPurchase.class.getClassLoader()))
          .status((BookingConfirmationStatus) in.readParcelable(BookingConfirmationStatus.class.getClassLoader()))
          .vehicle((BookingConfirmationImage) in.readParcelable(BookingConfirmationImage.class.getClassLoader()))
          .build();
    }

    @Override public BookingConfirmation[] newArray(int size) {
      return new BookingConfirmation[size];
    }
  };

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(actions());
    dest.writeParcelable(provider(), flags);
    dest.writeParcelable(purchase(), flags);
    dest.writeParcelable(status(), flags);
    dest.writeParcelable(vehicle(), flags);

  }

  @Override public int describeContents() {
    return 0;
  }

  public abstract List<BookingConfirmationAction> actions();
  @Nullable public abstract BookingConfirmationImage provider();
  public abstract BookingConfirmationPurchase purchase();
  public abstract BookingConfirmationStatus status();
  @Nullable public abstract BookingConfirmationImage vehicle();

}
