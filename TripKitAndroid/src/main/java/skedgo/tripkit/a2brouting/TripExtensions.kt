package skedgo.tripkit.a2brouting

import com.skedgo.android.common.model.SegmentType
import com.skedgo.android.common.model.Trip
import com.skedgo.android.common.model.TripSegment

/**
 * Gets a list of [TripSegment]s which are visible on the summary area of a [Trip].
 */
fun Trip.getSummarySegments(): List<TripSegment>
    = segments
    ?.filter { it.type != SegmentType.ARRIVAL }
    ?.filter { it.isVisibleInContext(TripSegment.VISIBILITY_IN_SUMMARY) }
    ?: emptyList()
