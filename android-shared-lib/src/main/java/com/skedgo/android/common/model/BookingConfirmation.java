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

      final ArrayList<String> trips = in.readArrayList(ArrayList.class.getClassLoader());

      return ImmutableBookingConfirmation.builder()
          .actions(actions)
          .trips(trips)
          .count(in.readInt())
          .index(in.readInt())
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
    dest.writeList(trips());
    dest.writeInt(count());
    dest.writeInt(index());
    dest.writeParcelable(provider(), flags);
    dest.writeParcelable(purchase(), flags);
    dest.writeParcelable(status(), flags);
    dest.writeParcelable(vehicle(), flags);
  }

  @Override public int describeContents() {
    return 0;
  }

  public abstract List<BookingConfirmationAction> actions();

  @Value.Default public int count() {
    return 0;
  }

  @Value.Default public int index() {
    return 0;
  }

  @Nullable public abstract BookingConfirmationImage provider();
  @Nullable public abstract BookingConfirmationPurchase purchase();
  public abstract BookingConfirmationStatus status();
  @Nullable public abstract BookingConfirmationImage vehicle();
  @Nullable public abstract List<String> trips();
}
