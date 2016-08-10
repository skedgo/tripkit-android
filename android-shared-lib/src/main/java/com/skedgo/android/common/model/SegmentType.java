package com.skedgo.android.common.model;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Locale;

public enum SegmentType {
  /* TODO: Should use int constants instead? */
    /* Arguably, https://medium.com/google-developers/developing-for-android-ii-bb9a51f8c8b9 */
  DEPARTURE, SCHEDULED, UNSCHEDULED, STATIONARY, ARRIVAL;

  @Nullable
  public static SegmentType from(String s) {
    if (TextUtils.isEmpty(s)) {
      return null;
    }

    final String lowerCase = s.toLowerCase(Locale.US);
    for (SegmentType value : values()) {
      if (TextUtils.equals(value.name().toLowerCase(Locale.US), lowerCase)) {
        return value;
      }
    }

    return null;
  }
}