package com.skedgo.tripkit.common.model.booking.confirmation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.tripkit.common.model.booking.confirmation.GsonAdaptersBookingConfirmationActionConfirmation;
import com.skedgo.tripkit.common.model.booking.confirmation.ImmutableBookingConfirmationActionConfirmation;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersBookingConfirmationActionConfirmation.class)
public abstract class BookingConfirmationActionConfirmation implements Parcelable {
    public static final Creator<BookingConfirmationActionConfirmation> CREATOR = new Creator<BookingConfirmationActionConfirmation>() {
        @Override
        public BookingConfirmationActionConfirmation createFromParcel(Parcel in) {
            return ImmutableBookingConfirmationActionConfirmation.builder()
                .message(in.readString())
                .abortActionTitle(in.readString())
                .confirmActionTitle(in.readString())
                .build();
        }

        @Override
        public BookingConfirmationActionConfirmation[] newArray(int size) {
            return new BookingConfirmationActionConfirmation[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message());
        dest.writeString(abortActionTitle());
        dest.writeString(confirmActionTitle());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public abstract String message();

    public abstract String abortActionTitle();

    public abstract String confirmActionTitle();
}
