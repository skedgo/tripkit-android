package skedgo.tripkit.ui.util;

import com.skedgo.android.common.StyleManager;

public class TimeSpanUtils {

  public static final String FORMAT_TIME_SPAN = "%s %s";
  public static final int HOURS_IN_DAY = 24 * 60;

  public static String getRelativeTimeSpanString(long minutes) {
    String timeUnit;

    int sign = minutes < 0 ? -1 : 1;
    long timeNumber = Math.abs(minutes);
    if (timeNumber == 0) {
      return StyleManager.FORMAT_TIME_SPAN_NOW;
    } else if (timeNumber < 60) {
      // Less than a hour
      timeUnit = StyleManager.FORMAT_TIME_SPAN_MIN;
    } else if (timeNumber >= HOURS_IN_DAY) {
      // More than a day
      timeNumber = Math.round((float) timeNumber / HOURS_IN_DAY);
      timeUnit = StyleManager.FORMAT_TIME_SPAN_DAY;
    } else {
      // Less than a day
      timeNumber = Math.round((float) timeNumber / 60);
      timeUnit = StyleManager.FORMAT_TIME_SPAN_HOUR;
    }

    return String.format(FORMAT_TIME_SPAN, sign * timeNumber, timeUnit);
  }
}