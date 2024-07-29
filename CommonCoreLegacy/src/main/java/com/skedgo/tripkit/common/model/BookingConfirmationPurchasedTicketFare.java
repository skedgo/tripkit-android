package com.skedgo.tripkit.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import androidx.annotation.Nullable;

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersBookingConfirmationPurchasedTicketFare.class)
public abstract class BookingConfirmationPurchasedTicketFare implements Parcelable {

    public static final Creator<BookingConfirmationPurchasedTicketFare> CREATOR = new Creator<BookingConfirmationPurchasedTicketFare>() {
        @Override
        public BookingConfirmationPurchasedTicketFare createFromParcel(Parcel in) {
            return ImmutableBookingConfirmationPurchasedTicketFare.builder()
                .id(in.readString())
                .currency(in.readString())
                .description(in.readString())
                .name(in.readString())
                .price(in.readDouble())
                .build();
        }

        @Override
        public BookingConfirmationPurchasedTicketFare[] newArray(int size) {
            return new BookingConfirmationPurchasedTicketFare[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currency());
        dest.writeString(description());
        dest.writeString(name());
        dest.writeDouble(price());
        dest.writeString(id());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Nullable
    public abstract String currency();

    @Nullable
    public abstract String description();

    @Nullable
    public abstract String name();

    public abstract Double price();

    public abstract String id();
}
