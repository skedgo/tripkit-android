package com.skedgo.android.common.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class RealtimeAlerts {
  private RealtimeAlerts() {}

  @Nullable public static String getDisplayText(@NonNull RealtimeAlert alert) {
    String text = alert.text();
    String title = alert.title();
    if (text != null && title != null) {
      if (text.startsWith(title)) {
        text = text.substring(title.length());
      }

      if (text.startsWith(" - ")) {
        text = text.substring(3);
      }
    }

    return text;
  }
}