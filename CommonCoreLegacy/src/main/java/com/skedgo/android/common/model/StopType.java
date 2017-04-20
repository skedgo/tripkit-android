package com.skedgo.android.common.model;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Locale;

public enum StopType {
  BUS("bus"),
  TRAIN("train"),
  FERRY("ferry"),
  MONORAIL("monorail"),
  SUBWAY("subway"),
  TAXI("taxi"),
  PARKING("parking"),
  TRAM("tram"),
  CABLECAR("cablecar");

  private String key;

  StopType(String key) {
    this.key = key;
  }

  @Nullable
  public static StopType from(String key) {
    if (TextUtils.isEmpty(key)) {
      return null;
    }

    final String lowerCase = key.toLowerCase(Locale.US);
    for (StopType value : values()) {
      if (TextUtils.equals(value.key, lowerCase)) {
        return value;
      }
    }

    return null;
  }

  @Override
  public String toString() {
    return key;
  }
}