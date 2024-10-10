package com.skedgo.tripkit.common.model.booking;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.tripkit.common.model.booking.GsonAdaptersBookingProvider;
import com.skedgo.tripkit.common.model.booking.ImmutableBookingProvider;
import com.skedgo.tripkit.routing.ServiceColor;

import androidx.annotation.Nullable;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersBookingProvider.class)
public abstract class BookingProvider implements Parcelable {
    public static final Creator<BookingProvider> CREATOR = new Creator<BookingProvider>() {
        @Override
        public BookingProvider createFromParcel(Parcel in) {
            return ImmutableBookingProvider.builder()
                .color((ServiceColor) in.readParcelable(ServiceColor.class.getClassLoader()))
                .name(in.readString())
                .phone(in.readString())
                .remoteDarkIcon(in.readString())
                .remoteIcon(in.readString())
                .website(in.readString())
                .build();
        }

        @Override
        public BookingProvider[] newArray(int size) {
            return new BookingProvider[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(color(), flags);
        dest.writeString(name());
        dest.writeString(phone());
        dest.writeString(remoteDarkIcon());
        dest.writeString(remoteIcon());
        dest.writeString(website());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Nullable
    public abstract ServiceColor color();

    @Nullable
    public abstract String name();

    @Nullable
    public abstract String phone();

    @Nullable
    public abstract String remoteDarkIcon();

    @Nullable
    public abstract String remoteIcon();

    @Nullable
    public abstract String website();
}
