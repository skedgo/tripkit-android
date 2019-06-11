package skedgo.tripkit.ui.timetable.services

import com.skedgo.android.common.util.TimeUtils
import javax.inject.Inject

open class TimeUtilsWrapper @Inject constructor() {

  open fun getTimeInDay(millisecs: Long): String =
      TimeUtils.getTimeInDay(millisecs * 1000)
}