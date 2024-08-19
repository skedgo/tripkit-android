package com.skedgo.tripkit.common.model;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.JsonAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersBookingConfirmationInputNew.class)
public abstract class BookingConfirmationInputNew implements Parcelable {

    public static final Creator<BookingConfirmationInputNew> CREATOR = new Creator<BookingConfirmationInputNew>() {
        @Override
        public BookingConfirmationInputNew createFromParcel(Parcel in) {

            List<BookingConfirmationInputOptions> options = new ArrayList<>();
            in.readTypedList(options, BookingConfirmationInputOptions.CREATOR);

            List<String> values = new ArrayList<>();
            in.readStringList(values);

            return ImmutableBookingConfirmationInputNew.builder()
                .id(in.readString())
                .required( VERSION.SDK_INT >= VERSION_CODES.Q ? in.readBoolean() : in.readByte() != 0)
                .title(in.readString())
                .type(in.readString())
                .options(options)
                .value(in.readString())
                .values(values)
                .build();
        }

        @Override
        public BookingConfirmationInputNew[] newArray(int size) {
            return new BookingConfirmationInputNew[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(options());
        dest.writeStringList(values());
        dest.writeString(id());
        dest.writeByte((byte) (required() ? 1 : 0));
        dest.writeString(title());
        dest.writeString(type());
        dest.writeString(value());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public abstract String id();

    public abstract boolean required();

    public abstract String title();

    public abstract String type();

    public abstract List<BookingConfirmationInputOptions> options();

    public abstract List<BookingConfirmationNotes> notes();

    @Nullable
    public abstract String value();

    @Nullable
    public abstract List<String> values();

    public List<String> obtainValue() {
        List<String> result = new ArrayList<String>();
        if (value() != null && !TextUtils.isEmpty(value())) {
            result.add(getValueFromOptions(value()));
        }
        if (values() != null && values().size() > 0) {
            for (String value : values()) {
                result.add(getValueFromOptions(value));
            }
        }

        return result;
    }

    private String getValueFromOptions(String key) {
        String result = key;
        for (BookingConfirmationInputOptions option : options()) {
            if (option.id().equals(key)) {
                result = option.title();
            }
        }

        return result;
    }
}
