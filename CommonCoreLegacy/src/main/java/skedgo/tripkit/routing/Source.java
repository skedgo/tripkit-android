package skedgo.tripkit.routing;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Source implements Parcelable {
  public abstract @Nullable String disclaimer();
  public abstract @Nullable Provider provider();

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(disclaimer());
    dest.writeParcelable(provider(), flags);
  }

  public static final Creator<Source> CREATOR = new Creator<Source>() {
    @Override public Source createFromParcel(Parcel in) {
      Provider provider = in.readParcelable(Provider.class.getClassLoader());
      return ImmutableSource.builder()
          .disclaimer(in.readString())
          .provider(provider)
          .build();
    }

    @Override public Source[] newArray(int size) {
      return new Source[size];
    }
  };
}
