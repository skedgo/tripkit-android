package com.skedgo.tripkit.routing;

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
@JsonAdapter(GsonAdaptersProvider.class)
public abstract class Provider implements Parcelable {
    public static final Creator<Provider> CREATOR = new Creator<Provider>() {
        @Override
        public Provider createFromParcel(Parcel in) {
            return ImmutableProvider.builder()
                .name(in.readString())
                .build();
        }

        @Override
        public Provider[] newArray(int size) {
            return new Provider[size];
        }
    };

    public abstract @Nullable String name();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }
}
