/*
 * Copyright (c) SkedGo 2013
 */
package com.skedgo.tripkit.common.util

import android.content.Context
import android.text.format.Time
import com.skedgo.tripkit.common.R
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit.SECONDS

object TimeUtils {
    private val TIME_LOCK = Any()

    //a very far future, in millis :) remember to check there are 13 digits (millis, not secs), last
    //character is letter L, not number '1'
    var WHEN_TELEPORTER_EXISTS: Long = 1777888999000L
    private val mTime = Time()

    fun getLastSecondOfPreviousDayAsTime(startsSecs: Long, timeZoneString: String?): Time {
        val startsMillis = startsSecs * 1000
        val offsetInSecs =
            ((TimeZone.getTimeZone(timeZoneString).getOffset(startsMillis)) / 1000).toLong()
        val julianDayOfInputTime = Time.getJulianDay(startsMillis, offsetInSecs)
        val prevDay = julianDayOfInputTime - 1
        val time = Time()
        time.timezone = timeZoneString
        time.normalize(false)
        time.setJulianDay(prevDay)
        time.normalize(false)
        time.hour = 23
        time.minute = 59
        time.second = 59
        time.normalize(false)
        return time
    }

    fun getMillisFrom(am_pm: String?, hour: Int, minute: Int, sec: Int): Long {
        var amPmInt = Calendar.AM
        if ("PM".equals(am_pm, ignoreCase = true)) {
            amPmInt = Calendar.PM
        }
        val calendar = Calendar.getInstance()
        calendar[Calendar.AM_PM] = amPmInt
        calendar[Calendar.HOUR] = hour
        calendar[Calendar.MINUTE] = minute
        return calendar.timeInMillis
    }

    val currentJulianDay: Int
        get() {
            synchronized(TIME_LOCK) {
                mTime.setToNow()
                return Time.getJulianDay(mTime.toMillis(true), mTime.gmtoff)
            }
        }

    fun getJulianDay(t: Time?): Int {
        return if (t == null) {
            0
        } else {
            Time.getJulianDay(t.toMillis(true), t.gmtoff)
        }
    }

    @JvmStatic
    fun getJulianDay(millis: Long): Int {
        synchronized(TIME_LOCK) {
            return Time.getJulianDay(millis, mTime.gmtoff)
        }
    }

    /***
     * @param millis
     * @return time in normal format, e.g, 3:05pm
     */
    fun getTimeInDay(millis: Long): String {
        synchronized(TIME_LOCK) {
            mTime.set(millis)
            return mTime.format("%l:%M%p")
        }
    }

    /**
     * The reason to have days is to prepare for interstate trip
     *
     * @return e.g, 1 day 2 hrs 30 mins
     */
    @JvmStatic
    fun getDurationInDaysHoursMins(context: Context, seconds: Int): String {
        return if (seconds > InSeconds.DAY) {
            getDurationWithDaysInIt(context, seconds)
        } else {
            getDurationInHoursMins(context, seconds)
        }
    }

    private fun getHrsAndMinsString(
        context: Context,
        hour: Int,
        minutes: Int,
        time: String
    ): String {
        var time = time
        if (hour == 1) {
            time += hour.toString() + " " + context.getString(R.string.str_hr)
        } else if (hour > 1) {
            time += hour.toString() + " " + context.getString(R.string.str_hrs)
        }

        if (minutes == 1) {
            if (hour >= 1) {
                time += " "
            }
            time += minutes.toString() + " " + context.getString(R.string.str_mins)
        } else if (minutes > 1) {
            if (hour >= 1) {
                time += " "
            }
            time += minutes.toString() + " " + context.getString(R.string.str_mins)
        }

        return time
    }

    fun getDurationInHoursMins(context: Context, seconds: Int): String {
        val hour = seconds / InSeconds.HOUR
        val minutes = (seconds % InSeconds.HOUR) / InSeconds.MINUTE
        val time = ""
        return getHrsAndMinsString(context, hour, minutes, time)
    }

    fun getDurationWithDaysInIt(context: Context, seconds: Int): String {
        val days = seconds / InSeconds.DAY
        val hour = (seconds % InSeconds.DAY) / InSeconds.HOUR
        val minutes = (seconds % InSeconds.HOUR) / InSeconds.MINUTE
        var time = ""
        if (days == 1) {
            time += "$days day"
        } else if (days > 1) {
            time += "$days days"
        }
        return getHrsAndMinsString(context, hour, minutes, time)
    }

    val currentMillis: Long
        get() {
            synchronized(TIME_LOCK) {
                mTime.setToNow()
                return mTime.toMillis(true)
            }
        }

    @JvmStatic
    fun getTimeZoneDisplayName(timezoneId: String?, timeInSecs: Long, locale: Locale?): String? {
        if (timezoneId == null) {
            return null
        }

        val timeZone = TimeZone.getTimeZone(timezoneId)

        // Unknown id.
        if (timeZone.id == "GMT") {
            return null
        }

        return timeZone.getDisplayName(
            timeZone.inDaylightTime(Date(SECONDS.toMillis(timeInSecs))),
            TimeZone.SHORT,
            locale
        )
    }

    object InMillis {
        const val SECOND: Long = 1000
        const val MINUTE: Long = SECOND * 60
        const val HOUR: Long = MINUTE * 60
        const val DAY: Long = HOUR * 24
        const val WEEK: Long = DAY * 7
        const val MONTH: Long = WEEK * 4
        const val YEAR: Long = MONTH * 12
    }

    object InSeconds {
        const val MINUTE: Int = 60
        const val HOUR: Int = MINUTE * 60
        const val DAY: Int = HOUR * 24
        const val WEEK: Int = DAY * 7
        const val MONTH: Int = WEEK * 4
        const val YEAR: Int = DAY * 365 // 31,536,000 < 2^32 (4.2 billion), an int is enough
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
    class Duration {
        var sign: Int = 1 // 1 or -1
        var weeks: Int = 0
        var days: Int = 0
        var hours: Int = 0
        var minutes: Int = 0
        var seconds: Int = 0

        /**
         * Parse according to RFC2445 ss4.3.6.  (It's actually a little loose with
         * its parsing, for better or for worse)
         */
        @Throws(Exception::class)
        fun parse(str: String) {
            sign = 1
            weeks = 0
            days = 0
            hours = 0
            minutes = 0
            seconds = 0

            val len = str.length
            var index = 0
            var c: Char

            if (len < 1) {
                return
            }

            c = str[0]
            if (c == '-') {
                sign = -1
                index++
            } else if (c == '+') {
                index++
            }

            if (len < index) {
                return
            }

            c = str[index]
            if (c != 'P') {
                throw Exception(
                    "Duration.parse(str='" + str + "') expected 'P' at index="
                        + index
                )
            }
            index++
            c = str[index]
            if (c == 'T') {
                index++
            }

            var n = 0
            while (index < len) {
                c = str[index]
                if (c >= '0' && c <= '9') {
                    n *= 10
                    n += c.code - '0'.code
                } else if (c == 'W') {
                    weeks = n
                    n = 0
                } else if (c == 'H') {
                    hours = n
                    n = 0
                } else if (c == 'M') {
                    minutes = n
                    n = 0
                } else if (c == 'S') {
                    seconds = n
                    n = 0
                } else if (c == 'D') {
                    days = n
                    n = 0
                } else if (c == 'T') {
                } else {
                    throw Exception(
                        "Duration.parse(str='" + str + "') unexpected char '"
                            + c + "' at index=" + index
                    )
                }
                index++
            }
        }

        val millis: Long
            get() {
                val factor = (1000 * sign).toLong()
                return factor * ((7 * 24 * 60 * 60 * weeks)
                    + (24 * 60 * 60 * days)
                    + (60 * 60 * hours)
                    + (60 * minutes)
                    + seconds)
            }
    }
}
