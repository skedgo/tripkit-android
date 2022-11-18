package com.skedgo.tripkit.regionrouting.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@Immutable
@TypeAdapters
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersOperator.class)
public abstract class Operator implements Parcelable {

    public static final Creator<Operator> CREATOR = new Creator<Operator>() {
        public Operator createFromParcel(Parcel in) {
            return ImmutableOperator.builder()
                    .id(in.readString())
                    .name(in.readString())
                    .build();
        }

        public Operator[] newArray(int size) {
            return new Operator[size];
        }
    };

    @Nullable
    public abstract String id();

    @Nullable
    public abstract String name();


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id());
        out.writeString(name());
    }
}