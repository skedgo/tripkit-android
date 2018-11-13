package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import skedgo.tripkit.routing.ServiceColor;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersPurchaseBrand.class)
public abstract class PurchaseBrand implements Parcelable {

  public static final Creator<PurchaseBrand> CREATOR = new Creator<PurchaseBrand>() {
    @Override public PurchaseBrand createFromParcel(Parcel in) {
      return ImmutablePurchaseBrand.builder()
          .color((ServiceColor) in.readParcelable(ServiceColor.class.getClassLoader()))
          .name(in.readString())
          .remoteIcon(in.readString())
          .build();
    }

    @Override public PurchaseBrand[] newArray(int size) {
      return new PurchaseBrand[size];
    }
  };

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(color(), flags);
    dest.writeString(name());
    dest.writeString(remoteIcon());
  }

  @Override public int describeContents() {
    return 0;
  }

  public abstract ServiceColor color();
  public abstract String name();
  @Nullable public abstract String remoteIcon();

}
