package com.skedgo.tripkit.routing

import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.routing.GroupVisibility.FULL
import java.util.Collections
import java.util.UUID

/**
 * Represents a list of [Trip]s. A list of [Trip]s comprises
 * of a display trip (aka representative trip) and alternative trips.
 * A display trip can be accessed via [.getDisplayTrip] while
 * alternative trips can be retrieved via [.getTrips] minus
 * [.getDisplayTrip]. That's because [.getTrips] returns
 * a list of [Trip]s including alternative trips and display trip.
 *
 *
 * Besides, a [TripGroup] also hold info related to [Source]
 * via [.getSources].
 */
class TripGroup {

    private var uuid = UUID.randomUUID().toString()
    var displayTripId: Long = 0

    var fullUrl: String = ""

    @SerializedName("sources")
    var sources: List<Source>? = null

    @SerializedName("trips")
    var trips: ArrayList<Trip>? = null
        private set

    @SerializedName("frequency")
    var frequency: Int = 0

    @Transient
    var visibility: GroupVisibility = FULL

    val displayTrip: Trip?
        get() {
            if (trips == null || trips!!.isEmpty()) {
                return null
            }

            for (trip in trips!!) {
                if (displayTripId == trip.tripId) {
                    return trip
                }
            }

            return trips!![0]
        }

    fun setTrips(trips: ArrayList<Trip?>?) {
        if (this.trips != null) {
            this.trips!!.clear()
        }

        if (trips == null) {
            this.trips = null
        } else {
            for (trip in trips) {
                addTrip(trip)
            }
        }
    }

    fun addTrip(trip: Trip?) {
        if (trip == null) {
            return
        }

        trip.group = this

        if (trips == null) {
            trips = ArrayList()
        }

        trips!!.add(trip)
    }

    /**
     * A sample use case: Add a trip computed by waypoint API into trip list.
     *
     * @param trip This trip must not belong to group's trips.
     * Otherwise, [IllegalStateException] will be thrown.
     * @throws IllegalStateException if the trip already belongs to the group.
     * To change display trip, should invoke [TripGroup.changeDisplayTrip] instead.
     */
    fun addAsDisplayTrip(trip: Trip) {
        check(!(trips != null && trips!!.contains(trip))) { "Trip already belongs to group" }

        // To avoid id conflict with the existing trips in trip list.
        val maxIdTrip = Collections.max(trips) { lhs, rhs ->
            TripComparators.compareLongs(
                lhs.tripId,
                rhs.tripId
            )
        }
        trip.tripId = maxIdTrip.tripId + 1

        // Don't call List<Trip>.add() but this.
        // We need to reference the group for each trip added.
        addTrip(trip)
        changeDisplayTrip(trip)
    }

    /**
     * @param trip This trip must belong to group's trips.
     * Otherwise, [IllegalStateException] will be thrown.
     * @return A TripGroup having new display trip.
     * @throws IllegalStateException if the trip doesn't belong to the group.
     */
    fun changeDisplayTrip(trip: Trip): TripGroup {
        check(!(trips != null && !trips!!.contains(trip))) { "Trip does not belong to group" }

        displayTripId = trip.tripId
        return this
    }

    fun uuid(uuid: String) {
        this.uuid = uuid
    }

    fun uuid(): String {
        return uuid
    }
}
