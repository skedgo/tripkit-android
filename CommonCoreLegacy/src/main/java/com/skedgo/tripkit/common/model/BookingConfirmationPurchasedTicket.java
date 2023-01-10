package com.skedgo.tripkit.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersBookingConfirmationPurchasedTicket.class)
public abstract class BookingConfirmationPurchasedTicket implements Parcelable {

    public static final Creator<BookingConfirmationPurchasedTicket> CREATOR = new Creator<BookingConfirmationPurchasedTicket>() {
        @Override
        public BookingConfirmationPurchasedTicket createFromParcel(Parcel in) {
            return ImmutableBookingConfirmationPurchasedTicket.builder()
                    .id(in.readString())
                    .status(in.readString())
                    .ticketURL(in.readString())
                    .activateURL(in.readString())
                    .fare((BookingConfirmationPurchasedTicketFare) in.readParcelable(BookingConfirmationPurchasedTicketFare.class.getClassLoader()))
                    .build();
        }

        @Override
        public BookingConfirmationPurchasedTicket[] newArray(int size) {
            return new BookingConfirmationPurchasedTicket[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status());
        dest.writeString(ticketURL());
        dest.writeString(id());
        dest.writeString(activateURL());
        dest.writeParcelable(fare(), flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Nullable public abstract String status();

    @Nullable public abstract String ticketURL();

    @Nullable public abstract String activateURL();

    @Nullable
    public abstract BookingConfirmationPurchasedTicketFare fare();

    public abstract String id();
}