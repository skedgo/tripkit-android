package com.skedgo.tripkit.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersBookingConfirmationInputOptions.class)
public abstract class BookingConfirmationInputOptions implements Parcelable {

    public static final Creator<BookingConfirmationInputOptions> CREATOR = new Creator<BookingConfirmationInputOptions>() {
        @Override
        public BookingConfirmationInputOptions createFromParcel(Parcel in) {
            return ImmutableBookingConfirmationInputOptions.builder()
                .id(in.readString())
                .title(in.readString())
                .build();
        }

        @Override
        public BookingConfirmationInputOptions[] newArray(int size) {
            return new BookingConfirmationInputOptions[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id());
        dest.writeString(title());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public abstract String id();

    public abstract String title();
}
