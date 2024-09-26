package com.skedgo.tripkit.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import org.immutables.value.Value;

import java.util.List;

import androidx.annotation.Nullable;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@Immutable
@TypeAdapters
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersStreet.class)
public abstract class Street implements Parcelable {
    public static final Creator<Street> CREATOR = new Creator<Street>() {
        @Override
        public Street createFromParcel(Parcel in) {
            return ImmutableStreet.builder()
                .name(in.readString())
                .metres(in.readFloat())
                .encodedWaypoints(in.readString())
                .safe(in.readByte() == 1)
                .dismount(in.readByte() == 1)
                .roadTags(in.createStringArrayList())
                .build();
        }

        @Override
        public Street[] newArray(int size) {
            return new Street[size];
        }
    };

    public abstract @Nullable String name();

    public abstract @Nullable String encodedWaypoints();

    @Value.Default
    public float metres() {
        return 0f;
    }

    @Value.Default
    public boolean safe() {
        return false;
    }

    @Value.Default
    public boolean dismount() {
        return false;
    }

    public abstract @Nullable List<String> roadTags();

    public abstract @Nullable Instruction instruction();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(name());
        dest.writeFloat(metres());
        dest.writeString(encodedWaypoints());
        dest.writeByte((byte) (safe() ? 1 : 0));
        dest.writeByte((byte) (dismount() ? 1 : 0));
        dest.writeStringList(roadTags());
    }

    public enum Instruction {
        @SerializedName("HEAD_TOWARDS") HEAD_TOWARDS,
        @SerializedName("CONTINUE_STRAIGHT") CONTINUE_STRAIGHT,
        @SerializedName("TURN_SLIGHTLY_LEFT") TURN_SLIGHTLY_LEFT,
        @SerializedName("TURN_LEFT") TURN_LEFT,
        @SerializedName("TURN_SHARPLY_LEFT") TURN_SHARPLY_LEFT,
        @SerializedName("TURN_SLIGHTLY_RIGHT") TURN_SLIGHTLY_RIGHT,
        @SerializedName("TURN_RIGHT") TURN_RIGHT,
        @SerializedName("TURN_SHARPLY_RIGHT") TURN_SHARPLY_RIGHT
    }
}
