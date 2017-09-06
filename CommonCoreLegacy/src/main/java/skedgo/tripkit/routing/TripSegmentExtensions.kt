package skedgo.tripkit.routing

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.support.annotation.MainThread
import com.skedgo.android.common.R
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit

/**
 * Gets a start date-time with time-zone.
 */
val TripSegment.startDateTime get(): DateTime = DateTime(
    TimeUnit.SECONDS.toMillis(startTimeInSecs),
    from.dateTimeZone
)

/**
 * Get an end date-time with time-zone.
 */
val TripSegment.endDateTime get(): DateTime = DateTime(
    TimeUnit.SECONDS.toMillis(endTimeInSecs),
    to.dateTimeZone
)

fun TripSegment.getLightTransportIcon(resources: Resources): Drawable {
  if (this.modeInfo != null && this.modeInfo!!.modeCompat != null) {
    return if (this.isRealTime)
      this.modeInfo!!.modeCompat.getRealtimeMapIconRes(resources)
    else
      this.modeInfo!!.modeCompat.getMapIconRes(resources)
  } else {
    return resources.createLightDrawable(R.drawable.v4_ic_map_location)
  }
}
