package com.skedgo.android.common.util;

import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.TestRunner;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static android.text.format.DateFormat.is24HourFormat;
import static com.skedgo.android.common.util.DateTimeFormats.printTime;
import static org.assertj.core.api.Java6Assertions.*;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class DateTimeFormatsTest2 {
  @Before public void before() {
    JodaTimeAndroid.init(RuntimeEnvironment.application);
  }

  @Test public void printTimeInChile() {
    final DateTimeZone zone = DateTimeZone.forID("America/Santiago");
    final DateTime dateTime = new DateTime(zone)
        .withYear(2016).withMonthOfYear(6).withDayOfMonth(20)
        .withHourOfDay(8).withMinuteOfHour(0);
    final String time = printTime(RuntimeEnvironment.application, dateTime.getMillis(), zone.getID());
    if (is24HourFormat(RuntimeEnvironment.application)) {
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
    final String time = printTime(RuntimeEnvironment.application, dateTime.getMillis(), zone.getID());
    if (is24HourFormat(RuntimeEnvironment.application)) {
      assertThat(time).isEqualTo("8:00");
    } else {
      assertThat(time).isEqualTo("8:00 AM");
    }
  }
}