package com.skedgo.tripkit.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersBookingConfirmationTickets.class)
public abstract class BookingConfirmationTickets implements Parcelable {

    public static final Creator<BookingConfirmationTickets> CREATOR = new Creator<BookingConfirmationTickets>() {
        @Override
        public BookingConfirmationTickets createFromParcel(Parcel in) {
            return ImmutableBookingConfirmationTickets.builder()
                    .currency(in.readString())
                    .description(in.readString())
                    .id(in.readString())
                    .name(in.readString())
                    .price(in.readDouble())
                    .value(in.readLong())
                    .build();
        }

        @Override
        public BookingConfirmationTickets[] newArray(int size) {
            return new BookingConfirmationTickets[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currency());
        dest.writeString(description());
        dest.writeString(id());
        dest.writeString(name());
        dest.writeDouble(price());
        dest.writeLong(value());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public abstract String currency();

    public abstract String description();

    public abstract String id();

    public abstract String name();

    public abstract Double price();

    public abstract Long value();
}
