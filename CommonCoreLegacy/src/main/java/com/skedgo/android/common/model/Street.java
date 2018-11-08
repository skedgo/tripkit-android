package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@Immutable
@TypeAdapters
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersStreet.class)
public abstract class Street implements Parcelable {
  public static final Creator<Street> CREATOR = new Creator<Street>() {
    @Override public Street createFromParcel(Parcel in) {
      return ImmutableStreet.builder()
          .name(in.readString())
          .meters(in.readFloat())
          .encodedWaypoints(in.readString())
          .safe(in.readByte() == 1)
          .build();
    }

    @Override public Street[] newArray(int size) {
      return new Street[size];
    }
  };

  public abstract @Nullable String name();
  public abstract @Nullable String encodedWaypoints();

  @Value.Default public float meters() {
    return 0f;
  }

  @Value.Default public boolean safe() {
    return false;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel dest, final int flags) {
    dest.writeString(name());
    dest.writeFloat(meters());
    dest.writeString(encodedWaypoints());
    dest.writeByte((byte) (safe() ? 1 : 0));
  }
}
