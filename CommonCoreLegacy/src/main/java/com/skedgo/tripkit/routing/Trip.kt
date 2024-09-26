package com.skedgo.tripkit.routing

import android.net.Uri
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.common.model.TransportMode
import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.common.model.time.ITimeRange
import org.joda.time.format.ISODateTimeFormat
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

/**
 * A [Trip] will mainly hold a list of [TripSegment]s which denotes
 * how to go from [Trip.getFrom] to [Trip.getTo].
 *
 *
 * Main use-cases:
 * - Trip's segments: [Trip.getSegments].
 * - Trip's start time: [TripExtensionsKt.getStartDateTime].
 * - Trip's end time: [TripExtensionsKt.getEndDateTime]}.
 * - Trip's costs: [.getCaloriesCost], [.getMoneyCost], [.getCarbonCost].
 */
class Trip : ITimeRange {
    companion object {
        const val UNKNOWN_COST = -9999.9999F
    }

    @SerializedName("segments")
    var rawSegmentList: ArrayList<JsonObject>? = null

    @SerializedName("currencySymbol")
    var currencySymbol: String? = null

    @SerializedName("saveURL")
    var saveURL: String? = null

    @SerializedName("depart")
    var depart: String? = null

    @SerializedName("arrive")
    var arrive: String? = null

    @SerializedName("caloriesCost")
    var caloriesCost: Float = 0f

    @SerializedName("moneyCost")
    var moneyCost: Float = UNKNOWN_COST

    @SerializedName("moneyUSDCost")
    var moneyUsdCost: Float = UNKNOWN_COST

    @SerializedName("carbonCost")
    var carbonCost: Float = 0f

    @SerializedName("hassleCost")
    var hassleCost: Float = 0f

    @SerializedName("weightedScore")
    var weightedScore: Float = 0f

    @SerializedName("updateURL")
    var updateURL: String? = null

    @SerializedName("progressURL")
    var progressURL: String? = null

    @SerializedName("plannedURL")
    var plannedURL: String? = null

    @SerializedName("temporaryURL")
    var temporaryURL: String? = null

    @SerializedName("logURL")
    var logURL: String? = null

    @SerializedName("shareURL")
    var shareURL: String? = null

    @SerializedName("subscribeURL")
    var subscribeURL: String? = null

    @SerializedName("unsubscribeURL")
    var unsubscribeURL: String? = null

    @SerializedName("mainSegmentHashCode")
    var mainSegmentHashCode: Long = 0

    @SerializedName("hideExactTimes")
    var hideExactTimes: Boolean = false

    @SerializedName("queryIsLeaveAfter")
    var queryIsLeaveAfter: Boolean = false

    @SerializedName("queryTime")
    var queryTime: Long = 0

    @SerializedName("availability")
    var mAvailability: String? = null

    var availabilityInfo: String? = null
    override var startTimeInSecs: Long = 0
        get() {
            if (field > 0) {
                return field
            }

            var millis: Long = -1
            try {
                millis = depart?.toLong() ?: -1
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (millis < 0 && depart != null) {
                millis = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(depart).millis
            }

            if (depart == null) {
                millis = 0
            }
            return millis
        }
    override var endTimeInSecs: Long = 0
        get() {
            if (field > 0) {
                return field
            }

            var millis: Long = -1
            try {
                millis = arrive?.toLong() ?: -1
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (millis < 0 && arrive != null) {
                millis = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(arrive).millis
            }

            if (arrive == null) {
                millis = 0
            }
            return millis
        }
    var uuid: String = UUID.randomUUID().toString()
    var id: String = ""
    var tripId: Long = 0
    @Transient var group: TripGroup? = null
    var isFavourite: Boolean = false
    var segmentList: ArrayList<TripSegment> = arrayListOf()
        set(segments) {
            field = segments
            field.forEach { it.setTrip(this) }
        }

    val from: Location?
        get() {
            return if (segmentList.isEmpty()) {
                null
            } else {
                val firstSeg = segmentList.first()
                firstSeg.from ?: firstSeg.singleLocation
            }
        }

    val quickBookingSegment: TripSegment?
        get() {
            return segmentList.firstOrNull { it.isQuickBooking }
        }

    val to: Location?
        get() {
            return if (segmentList.isEmpty()) {
                null
            } else {
                val lastSeg = segmentList.last()
                lastSeg.getTo() ?: lastSeg.getSingleLocation()
            }
        }

    constructor() {
        startTimeInSecs = 0
        endTimeInSecs = 0
        moneyCost = UNKNOWN_COST
        moneyUsdCost = UNKNOWN_COST
        carbonCost = 0f
        hassleCost = 0f
    }

    fun durationInSeconds(): Long {
        return endTimeInSecs - startTimeInSecs
    }

    fun getTimeCost(): Float {
        return (endTimeInSecs - startTimeInSecs).toFloat()
    }

    fun getAvailability(): Availability? {
        return mAvailability?.toAvailability()
    }

    fun setAvailability(availability: Availability) {
        this.mAvailability = availability.value
    }

    fun setAvailability(availability: String) {
        this.mAvailability = availability
    }

    fun getAvailabilityString(): String? {
        return mAvailability
    }

    fun getDisplayCost(localizedFreeText: String): String? {
        return when {
            moneyCost == 0f -> localizedFreeText
            moneyCost == UNKNOWN_COST -> null
            else -> {
                val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
                    roundingMode = RoundingMode.CEILING
                    maximumFractionDigits = 0
                }
                val value = numberFormat.format(moneyCost)
                "${currencySymbol ?: "$"}$value"
            }
        }
    }

    fun getDisplayCostUsd(): String? {
        return if (moneyUsdCost == 0f || moneyUsdCost == UNKNOWN_COST) {
            null
        } else {
            val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
                roundingMode = RoundingMode.CEILING
                maximumFractionDigits = 0
            }
            val value = numberFormat.format(moneyUsdCost)
            "${currencySymbol ?: "$"}$value"
        }
    }

    fun getDisplayCarbonCost(): String? {
        return if (carbonCost > 0) {
            "$carbonCost kg"
        } else {
            null
        }
    }

    fun getDisplayCalories(): String {
        return "$caloriesCost kcal"
    }

    fun getTripUuid(): String {
        return saveURL?.let {
            Uri.parse(it).lastPathSegment ?: it
        } ?: uuid
    }

    fun isMixedModal(ignoreWalking: Boolean): Boolean {
        var previousMode = ""
        segmentList.forEach { segment ->
            val currentMode = segment.getModeInfo()?.id ?: return@forEach
            if (segment.getType() != SegmentType.STATIONARY && !currentMode.isEmpty()) {
                if ((!segment.isWalking() || !ignoreWalking) && segment.getVisibility() == Visibilities.VISIBILITY_IN_SUMMARY) {
                    if (previousMode.isNotEmpty() && currentMode != previousMode) return true
                    previousMode = currentMode
                }
            }
        }
        return false
    }

    fun hasQuickBooking(): Boolean {
        return segmentList.any { it.getBooking()?.getQuickBookingsUrl() != null }
    }

    fun hasTransportMode(vararg modes: VehicleMode): Boolean {
        return segmentList.any { segment ->
            modes.any { mode -> segment.getMode() == mode }
        }
    }

    fun isDepartureTimeFixed(): Boolean {
        var hasPublicTransportSegment = false
        var hasNonFrequencyBasedSegment = false

        segmentList.forEach { segment ->
            if (!segment.serviceTripId.isNullOrEmpty()) {
                hasPublicTransportSegment = true
                if (segment.frequency == 0) {
                    hasNonFrequencyBasedSegment = true
                }
            }
        }

        return hasPublicTransportSegment && hasNonFrequencyBasedSegment
    }

    fun hasAnyPublicTransport(): Boolean {
        return segmentList.any { segment ->
            !segment.serviceTripId.isNullOrEmpty()
        }
    }

    fun hasAnyExpensiveTransport(): Boolean {
        return segmentList.any { segment ->
            when (segment.getTransportModeId()) {
                TransportMode.ID_AIR,
                TransportMode.ID_SHUFFLE,
                TransportMode.ID_TAXI,
                TransportMode.ID_TNC -> true
                else -> segment.transportModeId?.contains(TransportMode.MIDDLE_FIX_CAR) == true ||
                    segment.transportModeId?.contains(TransportMode.MIDDLE_FIX_BIC) == true
            }
        }
    }
}