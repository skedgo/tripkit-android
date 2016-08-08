package com.skedgo.android.common.util;

import android.support.test.runner.AndroidJUnit4;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.text.format.DateFormat.is24HourFormat;
import static com.skedgo.android.common.util.DateTimeFormats.printTime;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class DateTimeFormatsTest2 {
  @Before public void before() {
    JodaTimeAndroid.init(getTargetContext());
  }

  @Test public void printTimeInChile() {
    final DateTimeZone zone = DateTimeZone.forID("America/Santiago");
    final DateTime dateTime = new DateTime(zone)
        .withYear(2016).withMonthOfYear(6).withDayOfMonth(20)
        .withHourOfDay(8).withMinuteOfHour(0);
    final String time = printTime(getTargetContext(), dateTime.getMillis(), zone.getID());
    if (is24HourFormat(getTargetContext())) {
      assertThat(time).isEqualTo("8:00");
    } else {
      assertThat(time).isEqualTo("8:00 AM");
    }
  }

  @Test public void printTimeInSydney() {
    final DateTimeZone zone = DateTimeZone.forID("Australia/Sydney");
    final DateTime dateTime = new DateTime(zone)
        .withYear(2016).withMonthOfYear(6).withDayOfMonth(20)
        .withHourOfDay(8).withMinuteOfHour(0);
    final String time = printTime(getTargetContext(), dateTime.getMillis(), zone.getID());
    if (is24HourFormat(getTargetContext())) {
      assertThat(time).isEqualTo("8:00");
    } else {
      assertThat(time).isEqualTo("8:00 AM");
    }
  }
}