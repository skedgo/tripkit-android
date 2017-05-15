package skedgo.tripkit.routing;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Provider implements Parcelable {
  public abstract @Nullable String name();

  @Override public int describeContents() { return 0; }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name());
  }

  public static final Creator<Provider> CREATOR = new Creator<Provider>() {
    @Override public Provider createFromParcel(Parcel in) {
      return ImmutableProvider.builder()
          .name(in.readString())
          .build();
    }

    @Override public Provider[] newArray(int size) {
      return new Provider[size];
    }
  };
}
