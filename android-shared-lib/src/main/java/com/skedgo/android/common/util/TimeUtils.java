/*
 * Copyright (c) SkedGo 2013
 */

package com.skedgo.android.common.util;

import android.support.annotation.Nullable;
import android.text.format.Time;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TimeUtils {

  private static final Object TIME_LOCK = new Object();
  //a very far future, in millis :) remember to check there are 13 digits (millis, not secs), last
  //character is letter L, not number '1'
  public static long WHEN_TELEPORTER_EXISTS = 1777888999000l;
  private static Time mTime = new Time();

  public static Time getLastSecondOfPreviousDayAsTime(long startsSecs, String timeZoneString) {
    long startsMillis = startsSecs * 1000;
    long offsetInSecs = (TimeZone.getTimeZone(timeZoneString).getOffset(startsMillis)) / 1000;
    int julianDayOfInputTime = Time.getJulianDay(startsMillis, offsetInSecs);
    int prevDay = julianDayOfInputTime - 1;
    Time time = new Time();
    time.timezone = timeZoneString;
    time.normalize(false);
    time.setJulianDay(prevDay);
    time.normalize(false);
    time.hour = 23;
    time.minute = 59;
    time.second = 59;
    time.normalize(false);
    return time;
  }

  public static long getMillisFrom(String am_pm, int hour, int minute, int sec) {
    int amPmInt = Calendar.AM;
    if ("PM".equalsIgnoreCase(am_pm)) {
      amPmInt = Calendar.PM;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.AM_PM, amPmInt);
    calendar.set(Calendar.HOUR, hour);
    calendar.set(Calendar.MINUTE, minute);
    return calendar.getTimeInMillis();
  }

  public static int getCurrentJulianDay() {
    synchronized (TIME_LOCK) {
      mTime.setToNow();
      return Time.getJulianDay(mTime.toMillis(true), mTime.gmtoff);
    }
  }

  public static int getJulianDay(Time t) {
    if (t == null) {
      return 0;
    } else {
      return Time.getJulianDay(t.toMillis(true), t.gmtoff);
    }
  }

  public static int getJulianDay(long millis) {
    synchronized (TIME_LOCK) {
      return Time.getJulianDay(millis, mTime.gmtoff);
    }
  }

  /***
   * @param millis
   * @return time in normal format, e.g, 3:05pm
   */
  public static String getTimeInDay(long millis) {

    synchronized (TIME_LOCK) {
      mTime.set(millis);
      return mTime.format("%l:%M%p");
    }

  }

  /**
   * The reason to have days is to prepare for interstate trip
   *
   * @return e.g, 1 day 2 hrs 30 mins
   */
  public static String getDurationInDaysHoursMins(int seconds) {
    if (seconds > InSeconds.DAY) {
      return getDurationWithDaysInIt(seconds);
    } else {
      return getDurationInHoursMins(seconds);
    }
  }

  private static String getHrsAndMinsString(int hour, int minutes, String time) {
    if (hour == 1) {
      time += hour + " hr";
    } else if (hour > 1) {
      time += hour + " hrs";
    }

    if (minutes == 1) {
      if (hour >= 1) {
        time += " ";
      }
      time += minutes + " min";
    } else if (minutes > 1) {
      if (hour >= 1) {
        time += " ";
      }
      time += minutes + " mins";
    }

    return time;
  }

  public static String getDurationInHoursMins(int seconds) {
    int hour = seconds / InSeconds.HOUR;
    int minutes = (seconds % InSeconds.HOUR) / InSeconds.MINUTE;
    String time = "";
    return getHrsAndMinsString(hour, minutes, time);
  }

  public static String getDurationWithDaysInIt(int seconds) {
    int days = seconds / InSeconds.DAY;
    int hour = (seconds % InSeconds.DAY) / InSeconds.HOUR;
    int minutes = (seconds % InSeconds.HOUR) / InSeconds.MINUTE;
    String time = "";
    if (days == 1) {
      time += days + " day";
    } else if (days > 1) {
      time += days + " days";
    }
    return getHrsAndMinsString(hour, minutes, time);
  }

  public static long getCurrentMillis() {
    synchronized (TIME_LOCK) {
      mTime.setToNow();
      return mTime.toMillis(true);
    }
  }

  /**
   * This is NOT a 'time' util, but it's a waste of memory to create a new class for so few functions
   *
   * @param n max value
   * @return a random value between 0 and n
   */
  public static long nextLong(Random random, long n) {
    // Error checking and 2^x checking removed for simplicity.
    long bits, val;
    do {
      bits = (random.nextLong() << 1) >>> 1;
      val = bits % n;
    } while (bits - val + (n - 1) < 0L);
    return val;
  }

  @Nullable
  public static String getTimeZoneDisplayName(String timezoneId, long timeInSecs, Locale locale) {
    if (timezoneId == null) {
      return null;
    }

    TimeZone timeZone = TimeZone.getTimeZone(timezoneId);

    // Unknown id.
    if (timeZone.getID().equals("GMT")) {
      return null;
    }

    return timeZone.getDisplayName(
        timeZone.inDaylightTime(new Date(SECONDS.toMillis(timeInSecs))),
        TimeZone.SHORT,
        locale
    );
  }

  public static final class InMillis {
    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;
    public static final long WEEK = DAY * 7;
    public static final long MONTH = WEEK * 4;
    public static final long YEAR = MONTH * 12;
  }

  public static final class InSeconds {
    public static final int MINUTE = 60;
    public static final int HOUR = MINUTE * 60;
    public static final int DAY = HOUR * 24;
    public static final int WEEK = DAY * 7;
    public static final int MONTH = WEEK * 4;
    public static final int YEAR = DAY * 365; // 31,536,000 < 2^32 (4.2 billion), an int is enough
  }

    /*below class is copied out from android sdk source, weird that
    * I can't call the native Duration class in package package com.android.calendarcommon2;*/

  /**
   * According to RFC2445, durations are like this:
   * WEEKS
   * | DAYS [ HOURS [ MINUTES [ SECONDS ] ] ]
   * | HOURS [ MINUTES [ SECONDS ] ]
   * it doesn't specifically, say, but this sort of implies that you can't have
   * 70 seconds.
   */
  public static class Duration {
    public int sign; // 1 or -1
    public int weeks;
    public int days;
    public int hours;
    public int minutes;
    public int seconds;

    public Duration() {
      sign = 1;
    }

    /**
     * Parse according to RFC2445 ss4.3.6.  (It's actually a little loose with
     * its parsing, for better or for worse)
     */
    public void parse(String str) throws Exception {
      sign = 1;
      weeks = 0;
      days = 0;
      hours = 0;
      minutes = 0;
      seconds = 0;

      int len = str.length();
      int index = 0;
      char c;

      if (len < 1) {
        return;
      }

      c = str.charAt(0);
      if (c == '-') {
        sign = -1;
        index++;
      } else if (c == '+') {
        index++;
      }

      if (len < index) {
        return;
      }

      c = str.charAt(index);
      if (c != 'P') {
        throw new Exception(
            "Duration.parse(str='" + str + "') expected 'P' at index="
                + index);
      }
      index++;
      c = str.charAt(index);
      if (c == 'T') {
        index++;
      }

      int n = 0;
      for (; index < len; index++) {
        c = str.charAt(index);
        if (c >= '0' && c <= '9') {
          n *= 10;
          n += c - '0';
        } else if (c == 'W') {
          weeks = n;
          n = 0;
        } else if (c == 'H') {
          hours = n;
          n = 0;
        } else if (c == 'M') {
          minutes = n;
          n = 0;
        } else if (c == 'S') {
          seconds = n;
          n = 0;
        } else if (c == 'D') {
          days = n;
          n = 0;
        } else if (c == 'T') {
        } else {
          throw new Exception(
              "Duration.parse(str='" + str + "') unexpected char '"
                  + c + "' at index=" + index);
        }
      }
    }

    public long getMillis() {
      long factor = 1000 * sign;
      return factor * ((7 * 24 * 60 * 60 * weeks)
          + (24 * 60 * 60 * days)
          + (60 * 60 * hours)
          + (60 * minutes)
          + seconds);
    }
  }

}
