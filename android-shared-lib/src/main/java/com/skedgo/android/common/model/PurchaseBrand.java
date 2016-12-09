package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@JsonAdapter(GsonAdaptersPurchaseBrand.class)
public abstract class PurchaseBrand implements Parcelable {

  public static final Creator<PurchaseBrand> CREATOR = new Creator<PurchaseBrand>() {
    @Override public PurchaseBrand createFromParcel(Parcel in) {
      return ImmutablePurchaseBrand.builder()
          .color((ServiceColor) in.readParcelable(ServiceColor.class.getClassLoader()))
          .imageURL(in.readString())
          .build();
    }

    @Override public PurchaseBrand[] newArray(int size) {
      return new PurchaseBrand[size];
    }
  };

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(color(), flags);
    dest.writeString(imageURL());
  }

  @Override public int describeContents() {
    return 0;
  }

  public abstract ServiceColor color();
  public abstract String imageURL();

}
