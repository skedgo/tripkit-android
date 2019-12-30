package com.skedgo.tripkit.common;

import android.os.Parcel;
import android.os.Parcelable;

public final class Parcels {
  private Parcels() {}

  /**
   * An util method used to facilitate testing parcelling.
   * <p/>
   * Sample usage: `T actual = T.CREATOR.createFromParcel(Parcels.parcel(expected))`.
   */
  public static <T extends Parcelable> Parcel parcel(T original) {
    final Parcel parcel = Parcel.obtain();
    original.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    return parcel;
  }
}