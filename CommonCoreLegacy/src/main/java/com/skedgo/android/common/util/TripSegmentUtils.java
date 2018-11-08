package com.skedgo.android.common.util;

import android.content.Context;
import androidx.annotation.Nullable;

import com.skedgo.android.common.R;
import com.skedgo.android.common.model.Location;

import skedgo.tripkit.routing.SegmentActionTemplates;
import skedgo.tripkit.routing.TripSegment;

public final class TripSegmentUtils {
  private TripSegmentUtils() {}

  @Nullable
  public static String getTripSegmentAction(Context context, TripSegment segment) {
    String action = segment.getAction();
    if (action != null && action.contains(SegmentActionTemplates.TEMPLATE_DURATION)) {
      // Prepend a space due to https://redmine.buzzhives.com/issues/8971.
      action = processDurationTemplate(
          action,
          " " + context.getResources().getString(R.string.for__pattern),
          segment.getStartTimeInSecs(),
          segment.getEndTimeInSecs()
      );
    }

    String timezone = segment.getTimeZone();
    if (action != null && action.contains(SegmentActionTemplates.TEMPLATE_TIME)) {
      action = processTimeTemplate(
          context,
          action,
          timezone,
          segment.getStartTimeInSecs() * 1000
      );
    }

    return action;
  }

  @Nullable
  public static String processDurationTemplate(
      @Nullable String templateText,
      @Nullable String pattern,
      long startTimeInSecs,
      long endTimeInSecs) {
    if (templateText == null || !templateText.contains(SegmentActionTemplates.TEMPLATE_DURATION)) {
      return templateText;
    }

    long minutes = (endTimeInSecs - startTimeInSecs) / 60;
    if (minutes < 0) {
      templateText = templateText.replace(SegmentActionTemplates.TEMPLATE_DURATION, "");
    } else {
      templateText = templateText.replace(
          SegmentActionTemplates.TEMPLATE_DURATION,
          pattern != null
              ? String.format(pattern, getDurationFromMinutes(minutes))
              : getDurationFromMinutes(minutes)
      );
    }

    return templateText;
  }

  public static String processTimeTemplate(
      Context context,
      String templateText,
      String timezone,
      long timeInMillis) {
    if (templateText.contains(SegmentActionTemplates.TEMPLATE_TIME)) {
      String timeText = DateTimeFormats.printTime(context, timeInMillis, timezone);
      templateText = templateText.replace(SegmentActionTemplates.TEMPLATE_TIME, timeText);
    }

    return templateText;
  }

  /**
   * @param minutes number of seconds to format
   * @return A string formatted representation of the input
   * @see <a
   * href="http://byadrian.net/redmine/projects/buzzhives/wiki/Style_Guide#Time-and-durations">Redmine</a>
   */
  public static String getDurationFromMinutes(long minutes) {
    StringBuilder builder = new StringBuilder();
    if (minutes > 140) {
      if (((2 * minutes) / 60) % 2 == 0) {
        builder.append(((2 * minutes) / 60) / 2).append("h");
      } else {
        // Only get the first decimal place
        builder.append(withFirstDecimal((2 * minutes) / 60)).append("h");
      }
    } else if (minutes >= 60) {
      if ((minutes % 60) == 0) {
        builder.append(minutes / 60).append("h");
      } else if ((minutes % 30) == 0) {
        // Only get the first decimal place
        builder.append(withFirstDecimal(minutes / 60.0f)).append("h");
      } else if ((minutes % 60) == 1) {
        builder.append(minutes / 60).append("h 1min");
      } else {
        builder.append(minutes / 60).append("h ").append(minutes % 60).append("mins");
      }
    } else if (minutes == 0) {
      builder.append("< 1min");
    } else if (minutes != 1) {
      builder.append(minutes).append("mins");
    } else {
      builder.append("1min");
    }

    return builder.toString();
  }

  private static float withFirstDecimal(float f) {
    return Math.round(f * 10.0F) / 10.0F;
  }

  /**
   * TODO Should move this method into so-called 'LocationUtils'
   */
  public static Location getFirstNonNullLocation(Location... locations) {
    for (Location location : locations) {
      if (location != null) {
        return location;
      }
    }

    return null;
  }

  /**
   * TODO Should move this method into so-called 'LocationUtils'
   */
  public static String getLocationName(Location location) {
    return (location == null)
        ? null
        : StringUtils.firstNonEmpty(location.getAddress(), location.getName());
  }
}