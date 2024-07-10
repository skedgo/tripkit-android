package com.skedgo.tripkit.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;

import androidx.annotation.Nullable;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersBookingSource.class)
public abstract class BookingSource implements Parcelable {

    public static final Creator<BookingSource> CREATOR = new Creator<BookingSource>() {
        @Override
        public BookingSource createFromParcel(Parcel in) {
            return ImmutableBookingSource.builder()
                .provider((BookingProvider) in.readParcelable(BookingProvider.class.getClassLoader()))
                .build();
        }

        @Override
        public BookingSource[] newArray(int size) {
            return new BookingSource[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(provider(), flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Nullable
    public abstract BookingProvider provider();

}
