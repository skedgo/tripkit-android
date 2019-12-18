package com.skedgo.tripkit.routing;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersSource.class)
public abstract class Source implements Parcelable {
  public static final Creator<Source> CREATOR = new Creator<Source>() {
    @Override public Source createFromParcel(Parcel in) {
      return ImmutableSource.builder()
          .disclaimer(in.readString())
          .provider((Provider) in.readParcelable(Provider.class.getClassLoader()))
          .build();
    }

    @Override public Source[] newArray(int size) {
      return new Source[size];
    }
  };

  public abstract @Nullable String disclaimer();
  public abstract @Nullable Provider provider();

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(disclaimer());
    dest.writeParcelable(provider(), flags);
  }
}
