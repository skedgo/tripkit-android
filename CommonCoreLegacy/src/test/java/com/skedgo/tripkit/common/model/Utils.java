package com.skedgo.tripkit.common.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class Utils {
  private Utils() {}

  /**
   * An util method used to facilitate testing parcelling.
   * <p/>
   * Sample usage: `T actual = T.CREATOR.createFromParcel(Utils.parcel(expected))`.
   */
  public static <T extends Parcelable> Parcel parcel(T original) {
    final Parcel parcel = Parcel.obtain();
    original.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    return parcel;
  }
}