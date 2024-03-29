package com.skedgo.tripkit.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import java.util.ArrayList;
import java.util.List;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersBookingConfirmation.class)
public abstract class BookingConfirmation implements Parcelable {
    public static final Creator<BookingConfirmation> CREATOR = new Creator<BookingConfirmation>() {
        @Override
        public BookingConfirmation createFromParcel(Parcel in) {
            List<BookingConfirmationAction> actions = new ArrayList<>();
            in.readTypedList(actions, BookingConfirmationAction.CREATOR);

            List<BookingConfirmationInputNew> inputs = new ArrayList<>();
            in.readTypedList(inputs, BookingConfirmationInputNew.CREATOR);

            List<BookingConfirmationNotes> notes = new ArrayList<>();
            in.readTypedList(notes, BookingConfirmationNotes.CREATOR);

            List<BookingConfirmationTickets> tickets = new ArrayList<>();
            in.readTypedList(tickets, BookingConfirmationTickets.CREATOR);

            List<BookingConfirmationPurchasedTicket> purchasedTickets = new ArrayList<>();
            in.readTypedList(purchasedTickets, BookingConfirmationPurchasedTicket.CREATOR);

            return ImmutableBookingConfirmation.builder()
                    .actions(actions)
                    .provider((BookingConfirmationImage) in.readParcelable(BookingConfirmationImage.class.getClassLoader()))
                    .purchase((BookingConfirmationPurchase) in.readParcelable(BookingConfirmationPurchase.class.getClassLoader()))
                    .status((BookingConfirmationStatus) in.readParcelable(BookingConfirmationStatus.class.getClassLoader()))
                    .vehicle((BookingConfirmationImage) in.readParcelable(BookingConfirmationImage.class.getClassLoader()))
                    .input(inputs)
                    .notes(notes)
                    .tickets(tickets)
                    .purchasedTickets(purchasedTickets)
                    .build();
        }

        @Override
        public BookingConfirmation[] newArray(int size) {
            return new BookingConfirmation[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(actions());
        dest.writeParcelable(provider(), flags);
        dest.writeParcelable(purchase(), flags);
        dest.writeParcelable(status(), flags);
        dest.writeParcelable(vehicle(), flags);
        dest.writeTypedList(input());
        dest.writeTypedList(notes());
        dest.writeTypedList(tickets());
        dest.writeTypedList(purchasedTickets());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public abstract List<BookingConfirmationAction> actions();

    @Nullable
    public abstract BookingConfirmationImage provider();

    @Nullable
    public abstract BookingConfirmationPurchase purchase();

    public abstract BookingConfirmationStatus status();

    @Nullable
    public abstract BookingConfirmationImage vehicle();

    public abstract List<BookingConfirmationInputNew> input();

    public abstract List<BookingConfirmationNotes> notes();

    public abstract List<BookingConfirmationTickets> tickets();

    @Nullable public abstract List<BookingConfirmationPurchasedTicket> purchasedTickets();
}
