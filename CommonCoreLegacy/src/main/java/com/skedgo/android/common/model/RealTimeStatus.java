package com.skedgo.android.common.model;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Locale;

public enum RealTimeStatus {
  /* TODO: Should use int constants instead? */
    /* Arguably, https://medium.com/google-developers/developing-for-android-ii-bb9a51f8c8b9 */
  CAPABLE, IS_REAL_TIME, INCAPABLE;

  @Nullable
  public static RealTimeStatus from(String s) {
    if (TextUtils.isEmpty(s)) {
      return null;
    }

    final String lowerCase = s.toLowerCase(Locale.US);
    for (RealTimeStatus value : values()) {
      if (TextUtils.equals(lowerCase, value.name().toLowerCase(Locale.US))) {
        return value;
      }
    }

    return null;
  }
}