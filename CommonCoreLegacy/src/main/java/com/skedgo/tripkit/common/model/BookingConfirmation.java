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

            return ImmutableBookingConfirmation.builder()
                    .actions(actions)
                    .provider((BookingConfirmationImage) in.readParcelable(BookingConfirmationImage.class.getClassLoader()))
                    .purchase((BookingConfirmationPurchase) in.readParcelable(BookingConfirmationPurchase.class.getClassLoader()))
                    .status((BookingConfirmationStatus) in.readParcelable(BookingConfirmationStatus.class.getClassLoader()))
                    .vehicle((BookingConfirmationImage) in.readParcelable(BookingConfirmationImage.class.getClassLoader()))
                    .input(inputs)
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

}
