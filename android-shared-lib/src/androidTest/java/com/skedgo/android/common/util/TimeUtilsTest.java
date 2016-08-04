package com.skedgo.android.common.util;

import org.junit.Test;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeUtilsTest {
  // Most used languages
  private final Locale locale_en_AU = new Locale("en", "AU");
  private final Locale locale_en_US = new Locale("en", "US");
  private final Locale locale_en_GB = new Locale("en", "GB");
  private final Locale locale_zh_CN = new Locale("zh", "CN");
  private final Locale locale_zh_TW = new Locale("zh", "TW");

  @Test
  public void getTimezoneDisplayName_0() {
    assertThat(TimeUtils.getTimeZoneDisplayName(null, System.currentTimeMillis(),
                                                Locale.ENGLISH)).isNull();
  }

  @Test
  public void getTimezoneDisplayName_1() {
    assertThat(TimeUtils.getTimeZoneDisplayName("INVALID_ID", System.currentTimeMillis(),
                                                Locale.ENGLISH)).isNull();
  }

  @Test
  public void getTimezoneDisplayName_2() {
    final Calendar summerTime = Calendar.getInstance();
    summerTime.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
    summerTime.set(2015, Calendar.AUGUST, 10, 0, 0);

    assertThat(TimeUtils.getTimeZoneDisplayName("Australia/Sydney",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_en_AU)).isEqualTo("AEST");

    assertThat(TimeUtils.getTimeZoneDisplayName("Australia/Sydney",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_en_US)).isEqualTo("GMT+10:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("Australia/Sydney",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_en_GB)).isEqualTo("GMT+10:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("Australia/Sydney",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_zh_CN)).isEqualTo("GMT+10:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("Australia/Sydney",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_zh_TW)).isEqualTo("GMT+10:00");

    final Calendar winterTime = Calendar.getInstance();
    winterTime.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
    winterTime.set(2015, Calendar.FEBRUARY, 10, 0, 0);

    assertThat(TimeUtils.getTimeZoneDisplayName("Australia/Sydney",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_en_AU)).isEqualTo("AEDT");

    assertThat(TimeUtils.getTimeZoneDisplayName("Australia/Sydney",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_en_US)).isEqualTo("GMT+11:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("Australia/Sydney",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_en_GB)).isEqualTo("GMT+11:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("Australia/Sydney",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_zh_CN)).isEqualTo("GMT+11:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("Australia/Sydney",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_zh_TW)).isEqualTo("GMT+11:00");
  }

  @Test
  public void getTimezoneDisplayName_3() {
    final Calendar summerTime = Calendar.getInstance();
    summerTime.setTimeZone(TimeZone.getTimeZone("Europe/London"));
    summerTime.set(2015, Calendar.AUGUST, 10, 0, 0);

    assertThat(TimeUtils.getTimeZoneDisplayName("Europe/London",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_en_AU)).isEqualTo("BST");

    assertThat(TimeUtils.getTimeZoneDisplayName("Europe/London",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_en_US)).isEqualTo("GMT+01:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("Europe/London",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_en_GB)).isEqualTo("BST");

    assertThat(TimeUtils.getTimeZoneDisplayName("Europe/London",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_zh_CN)).isEqualTo("GMT+01:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("Europe/London",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_zh_TW)).isEqualTo("GMT+01:00");

    final Calendar winterTime = Calendar.getInstance();
    winterTime.setTimeZone(TimeZone.getTimeZone("Europe/London"));
    winterTime.set(2015, Calendar.FEBRUARY, 10, 0, 0);

    assertThat(TimeUtils.getTimeZoneDisplayName("Europe/London",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_en_AU)).isEqualTo("GMT+00:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("Europe/London",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_en_US)).isEqualTo("GMT+00:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("Europe/London",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_en_GB)).isEqualTo("GMT+00:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("Europe/London",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_zh_CN)).isEqualTo("GMT+00:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("Europe/London",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_zh_TW)).isEqualTo("GMT+00:00");
  }

  @Test
  public void getTimezoneDisplayName_4() {
    final Calendar summerTime = Calendar.getInstance();
    summerTime.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
    summerTime.set(2015, Calendar.AUGUST, 10, 0, 0);

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Buenos_Aires",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_en_AU)).isEqualTo("GMT-03:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Buenos_Aires",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_en_US)).isEqualTo("GMT-03:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Buenos_Aires",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_en_GB)).isEqualTo("GMT-03:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Buenos_Aires",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_zh_CN)).isEqualTo("GMT-03:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Buenos_Aires",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_zh_TW)).isEqualTo("GMT-03:00");

    final Calendar winterTime = Calendar.getInstance();
    winterTime.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
    winterTime.set(2015, Calendar.FEBRUARY, 10, 0, 0);

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Buenos_Aires",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_en_AU)).isEqualTo("GMT-03:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Buenos_Aires",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_en_US)).isEqualTo("GMT-03:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Buenos_Aires",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_en_GB)).isEqualTo("GMT-03:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Buenos_Aires",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_zh_CN)).isEqualTo("GMT-03:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Buenos_Aires",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_zh_TW)).isEqualTo("GMT-03:00");
  }

  @Test
  public void getTimezoneDisplayName_5() {
    final Calendar summerTime = Calendar.getInstance();
    summerTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
    summerTime.set(2015, Calendar.AUGUST, 10, 0, 0);

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Los_Angeles",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_en_AU)).isEqualTo("GMT-07:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Los_Angeles",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_en_US)).isEqualTo("PDT");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Los_Angeles",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_en_GB)).isEqualTo("GMT-07:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Los_Angeles",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_zh_CN)).isEqualTo("GMT-07:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Los_Angeles",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_zh_TW)).isEqualTo("GMT-07:00");

    final Calendar winterTime = Calendar.getInstance();
    winterTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
    winterTime.set(2015, Calendar.FEBRUARY, 10, 0, 0);

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Los_Angeles",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_en_AU)).isEqualTo("GMT-08:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Los_Angeles",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_en_US)).isEqualTo("PST");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Los_Angeles",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_en_GB)).isEqualTo("GMT-08:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Los_Angeles",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_zh_CN)).isEqualTo("GMT-08:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/Los_Angeles",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_zh_TW)).isEqualTo("GMT-08:00");
  }

  @Test
  public void getTimezoneDisplayName_6() {
    final Calendar summerTime = Calendar.getInstance();
    summerTime.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    summerTime.set(2015, Calendar.AUGUST, 10, 0, 0);

    assertThat(TimeUtils.getTimeZoneDisplayName("America/New_York",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_en_AU)).isEqualTo("GMT-04:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/New_York",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_en_US)).isEqualTo("EDT");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/New_York",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_en_GB)).isEqualTo("GMT-04:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/New_York",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_zh_CN)).isEqualTo("GMT-04:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/New_York",
                                                summerTime.getTimeInMillis() / 1000,
                                                locale_zh_TW)).isEqualTo("GMT-04:00");

    final Calendar winterTime = Calendar.getInstance();
    winterTime.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    winterTime.set(2015, Calendar.FEBRUARY, 10, 0, 0);

    assertThat(TimeUtils.getTimeZoneDisplayName("America/New_York",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_en_AU)).isEqualTo("GMT-05:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/New_York",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_en_US)).isEqualTo("EST");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/New_York",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_en_GB)).isEqualTo("GMT-05:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/New_York",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_zh_CN)).isEqualTo("GMT-05:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("America/New_York",
                                                winterTime.getTimeInMillis() / 1000,
                                                locale_zh_TW)).isEqualTo("GMT-05:00");
  }

  @Test
  public void getTimezoneDisplayName_7() {
    // London, daylight saving time changed on march 29, 2015, 1am

    final Calendar gmtLondonTime = Calendar.getInstance();
    gmtLondonTime.setTimeZone(TimeZone.getTimeZone("Europe/London"));
    gmtLondonTime.set(2015, Calendar.MARCH, 29, 0, 0);

    final Calendar bstLondonTime = Calendar.getInstance();
    bstLondonTime.setTimeZone(TimeZone.getTimeZone("Europe/London"));
    bstLondonTime.set(2015, Calendar.MARCH, 29, 2, 0);

    assertThat(TimeUtils.getTimeZoneDisplayName("Europe/London",
                                                gmtLondonTime.getTimeInMillis() / 1000,
                                                locale_en_GB)).isEqualTo("GMT+00:00");

    assertThat(TimeUtils.getTimeZoneDisplayName("Europe/London",
                                                bstLondonTime.getTimeInMillis() / 1000,
                                                locale_en_GB)).isEqualTo("BST");
  }
}