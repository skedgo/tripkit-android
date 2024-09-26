package com.skedgo.tripkit.common.model.booking.confirmation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.tripkit.common.model.booking.confirmation.GsonAdaptersBookingConfirmationStatus;
import com.skedgo.tripkit.common.model.booking.confirmation.ImmutableBookingConfirmationStatus;

import androidx.annotation.Nullable;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersBookingConfirmationStatus.class)
public abstract class BookingConfirmationStatus implements Parcelable {

    public static final Creator<BookingConfirmationStatus> CREATOR = new Creator<BookingConfirmationStatus>() {
        @Override
        public BookingConfirmationStatus createFromParcel(Parcel in) {
            return ImmutableBookingConfirmationStatus.builder()
                .title(in.readString())
                .subtitle(in.readString())
                .value(in.readString())
                .imageURL(in.readString())
                .build();
        }

        @Override
        public BookingConfirmationStatus[] newArray(int size) {
            return new BookingConfirmationStatus[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title());
        dest.writeString(subtitle());
        dest.writeString(value());
        dest.writeString(imageURL());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public abstract String title();

    @Nullable
    public abstract String subtitle();

    @Nullable
    public abstract String value();

    @Nullable
    public abstract String imageURL();
}
