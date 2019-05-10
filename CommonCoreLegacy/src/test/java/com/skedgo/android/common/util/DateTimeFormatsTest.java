package com.skedgo.android.common.util;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;

import static com.skedgo.android.common.util.DateTimeFormats.printTime;
import static org.assertj.core.api.Java6Assertions.assertThat;


public class DateTimeFormatsTest {
  @Before public void before() {
    JodaTimeAndroid.init(ApplicationProvider.getApplicationContext());
  }

  @Test public void printTimeInChile() {
    final DateTimeZone zone = DateTimeZone.forID("America/Santiago");
    final DateTime dateTime = new DateTime(zone)
        .withYear(2016).withMonthOfYear(6).withDayOfMonth(20)
        .withHourOfDay(8).withMinuteOfHour(0);
    final String time = printTime(ApplicationProvider.getApplicationContext(), dateTime.getMillis(), zone.getID());
    assertThat(time).isEqualTo("8:00 AM");
  }

  @Test public void printTimeInSydney() {
    final DateTimeZone zone = DateTimeZone.forID("Australia/Sydney");
    final DateTime dateTime = new DateTime(zone)
        .withYear(2016).withMonthOfYear(6).withDayOfMonth(20)
        .withHourOfDay(8).withMinuteOfHour(0);
    final String time = printTime(ApplicationProvider.getApplicationContext(), dateTime.getMillis(), zone.getID());
    assertThat(time).isEqualTo("8:00 AM");
  }
}