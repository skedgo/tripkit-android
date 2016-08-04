package com.skedgo.android.common.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateFormat;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public final class DateTimeFormats {
  private DateTimeFormats() {}

  public static String printTime(Context context, long millis, @Nullable String timezone) {
    final DateTime dateTime;
    if (!TextUtils.isEmpty(timezone)) {
      dateTime = new DateTime(millis, DateTimeZone.forID(timezone));
    } else {
      dateTime = new DateTime(millis, DateTimeZone.getDefault());
    }

    final DateTimeFormatter formatter = DateFormat.is24HourFormat(context)
        ? DateTimeFormat.forPattern("H:mm").withLocale(Locale.getDefault())
        : DateTimeFormat.forPattern("h:mm a").withLocale(Locale.getDefault());
    return formatter.print(dateTime);
  }
}