package com.skedgo.android.common.model;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Locale;

public enum RealTimeStatus {
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