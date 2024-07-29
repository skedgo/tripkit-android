package com.skedgo.tripkit.common.util;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.test.core.app.ApplicationProvider;

import static android.text.format.DateFormat.is24HourFormat;
import static com.skedgo.tripkit.common.util.DateTimeFormats.printTime;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class DateTimeFormatsTest2 {
    @Before
    public void before() {
        JodaTimeAndroid.init(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void printTimeInChile() {
        final DateTimeZone zone = DateTimeZone.forID("America/Santiago");
        final DateTime dateTime = new DateTime(zone)
            .withYear(2016).withMonthOfYear(6).withDayOfMonth(20)
            .withHourOfDay(8).withMinuteOfHour(0);
        final String time = printTime(ApplicationProvider.getApplicationContext(), dateTime.getMillis(), zone.getID());
        if (is24HourFormat(ApplicationProvider.getApplicationContext())) {
            assertThat(time).isEqualTo("8:00");
        } else {
            assertThat(time).isEqualTo("8:00 AM");
        }
    }

    @Test
    public void printTimeInSydney() {
        final DateTimeZone zone = DateTimeZone.forID("Australia/Sydney");
        final DateTime dateTime = new DateTime(zone)
            .withYear(2016).withMonthOfYear(6).withDayOfMonth(20)
            .withHourOfDay(8).withMinuteOfHour(0);
        final String time = printTime(ApplicationProvider.getApplicationContext(), dateTime.getMillis(), zone.getID());
        if (is24HourFormat(ApplicationProvider.getApplicationContext())) {
            assertThat(time).isEqualTo("8:00");
        } else {
            assertThat(time).isEqualTo("8:00 AM");
        }
    }
}