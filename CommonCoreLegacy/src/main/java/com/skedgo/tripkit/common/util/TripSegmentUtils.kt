package com.skedgo.tripkit.common.util

import android.content.Context
import com.skedgo.tripkit.common.R
import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.common.util.StringUtils.firstNonEmpty
import com.skedgo.tripkit.routing.SegmentActionTemplates
import com.skedgo.tripkit.routing.TripSegment

object TripSegmentUtils {
    @JvmStatic
    fun getTripSegmentAction(context: Context, segment: TripSegment): String? {
        var action = segment.action
        if (action != null && action.contains(SegmentActionTemplates.TEMPLATE_DURATION)) {
            // Prepend a space due to https://redmine.buzzhives.com/issues/8971.
            action = processDurationTemplate(
                context,
                action,
                " " + context.resources.getString(R.string.for__pattern),
                segment.getStartTimeInSecs(),
                segment.endTimeInSecs
            )
        }

        val timezone = segment.timeZone
        if (action != null && action.contains(SegmentActionTemplates.TEMPLATE_TIME)) {
            action = processTimeTemplate(
                context,
                action,
                timezone,
                segment.getStartTimeInSecs() * 1000
            )
        }

        return action
    }

    @JvmStatic
    fun processDurationTemplate(
        context: Context,
        templateText: String?,
        pattern: String?,
        startTimeInSecs: Long,
        endTimeInSecs: Long
    ): String? {
        var templateText = templateText
        if (templateText == null || !templateText.contains(SegmentActionTemplates.TEMPLATE_DURATION)) {
            return templateText
        }

        val minutes = (endTimeInSecs - startTimeInSecs) / 60
        templateText = if (minutes < 0) {
            templateText.replace(SegmentActionTemplates.TEMPLATE_DURATION, "")
        } else {
            templateText.replace(
                SegmentActionTemplates.TEMPLATE_DURATION,
                if (pattern != null
                ) String.format(
                    pattern,
                    getDurationFromMinutes(context, minutes)
                ) else getDurationFromMinutes(context, minutes)
            )
        }

        return templateText
    }

    fun processTimeTemplate(
        context: Context?,
        templateText: String,
        timezone: String?,
        timeInMillis: Long
    ): String {
        var templateText = templateText
        if (templateText.contains(SegmentActionTemplates.TEMPLATE_TIME)) {
            val timeText = DateTimeFormats.printTime(context, timeInMillis, timezone)
            templateText = templateText.replace(SegmentActionTemplates.TEMPLATE_TIME, timeText)
        }

        return templateText
    }

    /**
     * @param minutes number of seconds to format
     * @return A string formatted representation of the input
     * @see [Redmine](http://byadrian.net/redmine/projects/buzzhives/wiki/Style_Guide.Time-and-durations)
     */
    fun getDurationFromMinutes(context: Context, minutes: Long): String {
        val builder = StringBuilder()
        if (minutes > 140) {
            if (((2 * minutes) / 60) % 2 == 0L) {
                builder.append(((2 * minutes) / 60) / 2).append("h")
            } else {
                // Only get the first decimal place
                builder.append(withFirstDecimal(((2 * minutes) / 60).toFloat())).append("h")
            }
        } else if (minutes >= 60) {
            if ((minutes % 60) == 0L) {
                builder.append(minutes / 60).append("h")
            } else if ((minutes % 30) == 0L) {
                // Only get the first decimal place
                builder.append(withFirstDecimal(minutes / 60.0f)).append("h")
            } else if ((minutes % 60) == 1L) {
                builder.append(minutes / 60).append("h 1" + context.getString(R.string.str_mins))
            } else {
                builder.append(minutes / 60).append("h ").append(minutes % 60).append(
                    context.getString(
                        R.string.str_mins
                    )
                )
            }
        } else if (minutes == 0L) {
            builder.append("< 1" + context.getString(R.string.str_mins))
        } else if (minutes != 1L) {
            builder.append(minutes).append(context.getString(R.string.str_mins))
        } else {
            builder.append("1" + context.getString(R.string.str_mins))
        }

        return builder.toString()
    }

    private fun withFirstDecimal(f: Float): Float {
        return Math.round(f * 10.0f) / 10.0f
    }

    /**
     * TODO Should move this method into so-called 'LocationUtils'
     */
    @JvmStatic
    fun getFirstNonNullLocation(vararg locations: Location?): Location? {
        for (location in locations) {
            if (location != null) {
                return location
            }
        }

        return null
    }

    /**
     * TODO Should move this method into so-called 'LocationUtils'
     */
    @JvmStatic
    fun getLocationName(location: Location?): String? {
        return if ((location == null)
        ) null
        else firstNonEmpty(location.address, location.name)
    }
}