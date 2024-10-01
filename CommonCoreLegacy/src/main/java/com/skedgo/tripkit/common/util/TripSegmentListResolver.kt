package com.skedgo.tripkit.common.util

import android.content.res.Resources
import android.text.TextUtils
import com.skedgo.tripkit.common.R
import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.routing.SegmentType.ARRIVAL
import com.skedgo.tripkit.routing.SegmentType.DEPARTURE
import com.skedgo.tripkit.routing.TripSegment
import com.skedgo.tripkit.routing.Visibilities
import org.apache.commons.collections4.CollectionUtils
import java.util.concurrent.atomic.AtomicLong

/**
 * Puts a Departure segment before head of,
 * and puts an Arrival segment after tail of a segment list.
 *
 *
 * Also, fills identifiers for segments.
 */
class TripSegmentListResolver(private val resources: Resources) {
    private var origin: Location? = null
    private var destination: Location? = null
    private var tripSegmentList: MutableList<TripSegment>? = null
    private val segmentIdGenerator = AtomicLong()

    /**
     * @param origin The location that we depart
     */
    fun setOrigin(origin: Location?): TripSegmentListResolver {
        this.origin = origin
        return this
    }

    /**
     * @param destination The location that we finally arrive
     */
    fun setDestination(destination: Location?): TripSegmentListResolver {
        this.destination = destination
        return this
    }

    fun setTripSegmentList(tripSegmentList: MutableList<TripSegment>?): TripSegmentListResolver {
        this.tripSegmentList = tripSegmentList
        return this
    }

    fun resolve() {
        if (!CollectionUtils.isEmpty(tripSegmentList)) {
            putDepartureSegment()
            putArrivalSegment()

            fillSegmentIdentifiers()
        }
    }

    /**
     * Creates the Arrival segment from the last segment
     *
     * @param lastSegment The last segment (or tail) of the segment list
     * @return The Arrival segment
     */
    fun createArrivalSegment(lastSegment: TripSegment): TripSegment {
        val destinationName = TripSegmentUtils.getLocationName(destination)

        val arrivalAction = if (TextUtils.isEmpty(destinationName)) {
            String.format(
                resources.getString(R.string.arrive_at__pattern),
                resources.getString(R.string.destination)
            )
        } else {
            String.format(
                resources.getString(R.string.arrive_at__pattern),
                destinationName
            )
        }

        val arrivalSegment = TripSegment()
        arrivalSegment.type = ARRIVAL
        arrivalSegment.from = destination
        arrivalSegment.to = destination
        arrivalSegment.action = arrivalAction
        arrivalSegment.visibility = Visibilities.VISIBILITY_ON_MAP
        arrivalSegment.startTimeInSecs = lastSegment.endTimeInSecs
        arrivalSegment.endTimeInSecs = lastSegment.endTimeInSecs
        arrivalSegment.availability = lastSegment.availability
        return arrivalSegment
    }

    /**
     * Creates the Departure segment from the first segment
     *
     * @param firstSegment The first segment (or head) of the segment list
     * @return The Departure segment
     */
    fun createDepartureSegment(firstSegment: TripSegment): TripSegment {
        val originName = TripSegmentUtils.getLocationName(origin)
        val departureAction = if (TextUtils.isEmpty(originName)) {
            String.format(
                resources.getString(R.string.leave__pattern),
                resources.getString(R.string.origin)
            )
        } else {
            String.format(
                resources.getString(R.string.leave__pattern),
                originName
            )
        }

        val departureSegment = TripSegment()
        departureSegment.type = DEPARTURE
        departureSegment.from = origin
        departureSegment.to = origin
        departureSegment.action = departureAction
        departureSegment.visibility = Visibilities.VISIBILITY_IN_DETAILS
        departureSegment.startTimeInSecs = firstSegment.getStartTimeInSecs()
        departureSegment.endTimeInSecs = firstSegment.getStartTimeInSecs()
        departureSegment.availability = firstSegment.availability
        return departureSegment
    }

    private fun fillSegmentIdentifiers() {
        segmentIdGenerator.set(0L)
        var newSegmentId: Long
        for (segment in tripSegmentList!!) {
            newSegmentId = segmentIdGenerator.incrementAndGet()
            segment!!.id = newSegmentId
        }
    }

    /**
     * Puts a Departure segment before head
     */
    private fun putArrivalSegment() {
        val lastSegment = tripSegmentList!![tripSegmentList!!.size - 1]
        if (lastSegment != null) {
            val arrivalSegment = createArrivalSegment(lastSegment)
            tripSegmentList!!.add(arrivalSegment)
        }
    }

    /**
     * Puts an Arrival segment after tail
     */
    private fun putDepartureSegment() {
        val firstSegment = tripSegmentList!![0]
        if (firstSegment != null) {
            val departureSegment = createDepartureSegment(firstSegment)
            tripSegmentList!!.add(0, departureSegment)
        }
    }
}