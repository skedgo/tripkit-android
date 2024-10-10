package com.skedgo.tripkit.common.model.booking.confirmation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.tripkit.common.model.booking.confirmation.GsonAdaptersBookingConfirmationNotes;
import com.skedgo.tripkit.common.model.booking.confirmation.ImmutableBookingConfirmationNotes;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersBookingConfirmationNotes.class)
public abstract class BookingConfirmationNotes implements Parcelable {

    public static final Creator<BookingConfirmationNotes> CREATOR = new Creator<BookingConfirmationNotes>() {
        @Override
        public BookingConfirmationNotes createFromParcel(Parcel in) {
            return ImmutableBookingConfirmationNotes.builder()
                .provider(in.readString())
                .text(in.readString())
                .timestamp(in.readString())
                .build();
        }

        @Override
        public BookingConfirmationNotes[] newArray(int size) {
            return new BookingConfirmationNotes[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(provider());
        dest.writeString(text());
        dest.writeString(timestamp());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public abstract String provider();

    public abstract String text();

    public abstract String timestamp();
}