package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersBookingConfirmationAction.class)
public abstract class BookingConfirmationAction implements Parcelable {

  public static final String TYPE_CANCEL = "CANCEL";
  public static final String TYPE_CALL = "CALL";

  public static final Creator<BookingConfirmationAction> CREATOR = new Creator<BookingConfirmationAction>() {
    @Override public BookingConfirmationAction createFromParcel(Parcel in) {
      return ImmutableBookingConfirmationAction.builder()
          .internalURL(in.readString())
          .externalURL(in.readString())
          .isDestructive(in.readByte() != 0)
          .title(in.readString())
          .type(in.readString())
          .build();
    }

    @Override public BookingConfirmationAction[] newArray(int size) {
      return new BookingConfirmationAction[size];
    }
  };

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(internalURL());
    dest.writeString(externalURL());
    dest.writeByte((byte) (isDestructive() ? 1 : 0));
    dest.writeString(title());
    dest.writeString(type());
  }

  @Override public int describeContents() {
    return 0;
  }

  @Nullable public abstract String internalURL();
  @Nullable public abstract String externalURL();
  public abstract boolean isDestructive();
  public abstract String title();
  public abstract String type();

}
