package com.skedgo.android.tripkit.booking.viewmodel;

import android.os.Parcelable;

import com.skedgo.android.tripkit.booking.InputForm;

public interface Param extends Parcelable {
  String getMethod();
  String getUrl();
  String getHudText();
  InputForm postBody();
}
