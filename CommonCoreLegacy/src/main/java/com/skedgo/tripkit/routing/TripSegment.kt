package com.skedgo.tripkit.routing

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.common.R
import com.skedgo.tripkit.common.StyleManager
import com.skedgo.tripkit.common.agenda.IRealTimeElement
import com.skedgo.tripkit.common.model.MiniInstruction
import com.skedgo.tripkit.common.model.SharedVehicle
import com.skedgo.tripkit.common.model.Street
import com.skedgo.tripkit.common.model.TransportMode
import com.skedgo.tripkit.common.model.booking.Booking
import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.common.model.realtimealert.RealtimeAlert
import com.skedgo.tripkit.common.model.time.ITimeRange
import com.skedgo.tripkit.common.util.TimeUtils.getDurationInDaysHoursMins
import com.skedgo.tripkit.common.util.TripSegmentUtils.getFirstNonNullLocation
import com.skedgo.tripkit.common.util.TripSegmentUtils.processDurationTemplate
import com.skedgo.tripkit.routing.SegmentType.SCHEDULED
import com.skedgo.tripkit.routing.SegmentType.STATIONARY
import org.joda.time.format.ISODateTimeFormat
import java.util.Locale

/**
 * To go from A to B, sometimes we have to travel X, Y, Z locations between A and B.
 * That means, we travel A to X, then X to Y, then Y to Z, then Z to B which is the destination.
 * To show how to travel from A to X, we use [TripSegment]. So, in this case,
 * a trip from A to B comprises 6 segments:
 * - A segment whose type is [SegmentType.DEPARTURE].
 * - A segment from A to X.
 * - A segment from X to Y.
 * - A segment from Y to Z.
 * - A segment from Z to B.
 * - A segment whose type is [SegmentType.ARRIVAL].
 *
 *
 * Note that, to avoid [TransactionTooLargeException], it's discouraged to
 * pass any instance of [Query] to [Intent] or [Bundle].
 * The [Parcelable] is subject to deletion at anytime.
 *
 * @see [Trips, groups, frequencies and templates](http://skedgo.github.io/tripgo-api/site/faq/.trips-groups-frequencies-and-templates)
 */
class TripSegment : IRealTimeElement, ITimeRange {

    var id: String = ""
    var segmentId: Long = 0

    @Transient
    var trip: Trip? = null

    @JvmField
    @SerializedName("booking")
    var booking: Booking? = null

    @SerializedName("bookingHashCode")
    var bookingHashCode: Long? = null

    @SerializedName("mini")
    val miniInstruction: MiniInstruction? = null

    @SerializedName("sharedVehicle")
    var sharedVehicle: SharedVehicle? = null
        set(vehicle) {
            field = field
        }

    /**
     * @see [Mode Identifiers](http://skedgo.github.io/tripgo-api/site/faq/.mode-identifiers)
     */
    @JvmField
    @SerializedName("modeInfo")
    var modeInfo: ModeInfo? = null

    @SerializedName("type")
    private val type: String? = null

    private var mType: SegmentType? = null

    @SerializedName("startTime")
    private val startTime: String? = null

    @SerializedName("endTime")
    private val endTime: String? = null

    @SerializedName("visibility")
    var visibility: String? = null

    @SerializedName("from")
    var from: Location? = null

    @SerializedName("to")
    var to: Location? = null

    @SerializedName("location")
    var singleLocation: Location? = null

    @SerializedName("action")
    var action: String? = null

    @SerializedName("travelDirection")
    var direction: Int = 0

    /**
     * NOTE: For unscheduled segments, if we want to show notes on views, don't call this.
     * Call `getDisplayNotes` instead.
     */
    @SerializedName("notes")
    var notes: String? = null

    @SerializedName("serviceIsFrequencyBased")
    var isFrequencyBased: Boolean = false
        private set

    @SerializedName("frequency")
    var frequency: Int = 0

    @SerializedName("serviceTripID")
    private var mServiceTripId: String? = null

    @SerializedName("serviceName")
    var serviceName: String? = null

    @SerializedName("serviceColor")
    var serviceColor: ServiceColor? = null

    @SerializedName("serviceNumber")
    var serviceNumber: String? = null

    @SerializedName("serviceOperator")
    var serviceOperator: String? = null

    @SerializedName("stopCode")
    private var mStartStopCode: String? = null

    @SerializedName("endStopCode")
    private var mEndStopCode: String? = null

    @SerializedName("streets")
    private var mStreets: ArrayList<Street>? = null

    @SerializedName("shapes")
    var shapes: ArrayList<Shape>? = null

    @SerializedName("realtimeVehicle")
    var realTimeVehicle: RealTimeVehicle? = null

    @JvmField
    @SerializedName("wheelchairAccessible")
    var wheelchairAccessible: Boolean = false

    @SerializedName("bicycleAccessible")
    var bicycleAccessible: Boolean = false

    @JvmField
    @SerializedName("localCost")
    val localCost: LocalCost? = null

    @SerializedName("timetableStartTime")
    var timetableStartTime: Long = 0

    @SerializedName("timetableEndTime")
    var timetableEndTime: Long = 0

    @SerializedName("availability")
    var availability: String? = null

    override var startTimeInSecs: Long = 0
        get() {
            if (field > 0) {
                return field
            }

            var millis = -1L
            try {
                millis = startTime!!.toLong()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (millis < 0 && startTime != null) {
                millis = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(startTime).millis
            }

            if (startTime == null) {
                millis = 0
            }
            return millis
        }

    override fun getStartTimeInSeconds(): Long {
        return startTimeInSecs
    }

    override var endTimeInSecs: Long = 0
        get() {
            if (field > 0) {
                return field
            }

            var millis = -1L
            try {
                millis = endTime!!.toLong()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (millis < 0 && endTime != null) {
                millis = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(endTime).millis
            }

            if (endTime == null) {
                millis = 0
            }
            return millis
        }

    var isHideExactTimes: Boolean = false
    var geofences: List<Geofence>? = null

    /**
     * This is no longer a part of json returned from server due to Version 6.
     * It's currently being used for json-based persistence on app local.
     *
     * @see [Version 6](https://redmine.buzzhives.com/projects/buzzhives/wiki/Private_APIs.Version-6)
     */
    @SerializedName("alerts")
    var alerts: ArrayList<RealtimeAlert>? = null

    @SerializedName("isContinuation")
    var isContinuation: Boolean = false

    @SerializedName("serviceDirection")
    var serviceDirection: String? = null
    /**
     * Segments having type as [SegmentType.DEPARTURE], [SegmentType.ARRIVAL],
     * and [SegmentType.STATIONARY] will have this property as null.
     *
     *
     * For more information about the transport. Please check out [TripSegment.getModeInfo].
     *
     * @see [Mode Identifiers](http://skedgo.github.io/tripgo-api/site/faq/.mode-identifiers)
     */
    /**
     * NOTE: Values are defined by 'modes' in https://api.tripgo.com/v1/regions.json
     */
    @JvmField
    @SerializedName("modeIdentifier")
    var transportModeId: String? = null

    /**
     * Currently used for shuttle buses which explains how often they run
     */
    @SerializedName("terms")
    var terms: String? = null

    /**
     * Indicates if the times are real-time or not.
     *
     * @see [Version 9](https://redmine.buzzhives.com/projects/buzzhives/wiki/Private_APIs.Version-9-next)
     *
     * @see [Issue 3931](https://redmine.buzzhives.com/issues/3931)
     */
    @SerializedName("realTime")
    var isRealTime: Boolean = false

    /**
     * @see [Version 9](https://redmine.buzzhives.com/projects/buzzhives/wiki/Private_APIs.Version-9-next)
     *
     * @see [Issue 3931](https://redmine.buzzhives.com/issues/3931)
     */
    @SerializedName("durationWithoutTraffic")
    var durationWithoutTraffic: Long = 0

    @JvmField
    @SerializedName("segmentTemplateHashCode")
    var templateHashCode: Long = 0

    @SerializedName("alertHashCodes")
    var alertHashCodes: LongArray = longArrayOf()

    @JvmField
    @SerializedName("platform")
    var platform: String? = null

    @JvmField
    @SerializedName("stops")
    var stopCount: Int = 0

    @JvmField
    @SerializedName("metres")
    var metres: Int = 0

    @JvmField
    @SerializedName("metresSafe")
    var metresSafe: Int = 0

    @SerializedName("metresUnsafe")
    var metresUnsafe: Int = 0

    @SerializedName("turn-by-turn")
    val turnByTurn: String? = null

    @SerializedName("hasCarParks")
    private var hasCarParks = false

    @SerializedName("ticket")
    val ticket: Ticket? = null

    @SerializedName("ticketWebsiteURL")
    val ticketURL: String? = null

    @SerializedName("tickets")
    private val tickets: List<Ticket>? = null

    var mapTiles: TripKitMapTiles? = null

    val streets: List<Street>?
        get() = mStreets

    fun setStreets(streets: ArrayList<Street>?) {
        mStreets = streets
    }

    fun getType(): SegmentType? {
        if (type == null) {
            return mType
        }
        return type.from()
    }

    fun setType(type: SegmentType?) {
        mType = type
    }

    @get:Deprecated("")
    val mode: VehicleMode?
        get() = if (modeInfo != null) modeInfo!!.modeCompat else null

    fun isFrequencyBased(isFrequencyBased: Boolean) {
        this.isFrequencyBased = isFrequencyBased
    }

    override fun getStartStopCode(): String? = mStartStopCode

    override fun setStartStopCode(startStopCode: String?) {
        mStartStopCode = startStopCode
    }

    override fun getEndStopCode(): String? = mEndStopCode

    override fun setEndStopCode(endStopCode: String?) {
        mEndStopCode = endStopCode
    }

    fun setServiceTripId(id: String?) {
        mServiceTripId = id
    }
    override fun getServiceTripId(): String? = mServiceTripId

    override fun getOperator(): String {
        return serviceOperator!!
    }

    val wheelchairFriendliness: Int
        get() = Math.round(metresSafe / metres.toFloat() * 100)

    val cycleFriendliness: Int
        get() = Math.round(metresSafe / metres.toFloat() * 100)

    val pairIdentifier: String
        get() = String.format(
            StyleManager.FORMAT_PAIR_IDENTIFIER,
            mStartStopCode,
            mEndStopCode
        )

    val isStationary: Boolean
        get() = getType() == null || getType() == STATIONARY

    val timeZone: String?
        get() {
            val location = getFirstNonNullLocation(
                this.from,
                this.singleLocation
            )
            if (location != null) {
                return location.timeZone
            }
            return null
        }

    fun isVisibleInContext(contextVisibility: String?): Boolean {
        if (visibility == null || visibility!!.isEmpty() || contextVisibility == null || contextVisibility.isEmpty() || visibility == Visibilities.VISIBILITY_HIDDEN || contextVisibility == Visibilities.VISIBILITY_HIDDEN) {
            return false
        }

        return if (contextVisibility == Visibilities.VISIBILITY_IN_SUMMARY) {
            visibility == Visibilities.VISIBILITY_ON_MAP || visibility == Visibilities.VISIBILITY_IN_SUMMARY
        } else if (contextVisibility == Visibilities.VISIBILITY_ON_MAP) {
            visibility != Visibilities.VISIBILITY_IN_DETAILS
        } else if (contextVisibility == Visibilities.VISIBILITY_IN_DETAILS) {
            // Show segment in details, no matter what the type
            true
        } else {
            true
        }
    }

    /**
     * As of now, used only for unscheduled segments.
     * If we want to show notes on views, use it instead of `getNotes`.
     * Some essential templates will be resolved before being presented by the views.
     */
    fun getDisplayNotes(context: Context, withPlatform: Boolean): String? {
        var notes = processDurationTemplate(
            context,
            notes,
            null,
            if (startTimeInSecs == 0L && startTime!!.toLong() > 0) startTime.toLong() else startTimeInSecs,
            if (endTimeInSecs == 0L && endTime!!.toLong() > 0) endTime.toLong() else endTimeInSecs
        )

        if (notes == null) {
            return null
        }


        notes = if (platform != null && withPlatform) {
            notes.replace(
                Templates.TEMPLATE_PLATFORM,
                String.format(Templates.FORMAT_PLATFORM, platform)
            )
        } else {
            notes.replace(Templates.TEMPLATE_PLATFORM + "\n", "")
        }

        notes = notes.replace(Templates.TEMPLATE_STOPS, convertStopCountToText(context, stopCount))

        if (durationWithoutTraffic == 0L) {
            return notes
        }

        val durationWithTraffic = endTimeInSecs - startTimeInSecs
        return if (durationWithoutTraffic < durationWithTraffic + 60 /* secs */) {
            // Plus 60 secs since we show both duration types in minutes.
            // For instance, if durationWithTraffic is 65 secs, and durationWithoutTraffic is 60 secs,
            // they will be both shown as '1min'. Thus, no need to show this difference.
            notes.replace(Templates.TEMPLATE_TRAFFIC, getDurationWithoutTrafficText(context))
        } else {
            // TODO: Probably we also need to remove a redundant dot char next to the template.
            notes.replace(Templates.TEMPLATE_TRAFFIC, "")
        }
    }

    fun getRealTimeStatusText(resources: Resources): String? {
        return if (isRealTime) {
            if (mode != null && mode!!.isPublicTransport) {
                resources.getString(R.string.real_minustime)
            } else {
                resources.getString(R.string.live_traffic)
            }
        } else {
            null
        }
    }

    @get:DrawableRes
    val darkVehicleIcon: Int
        get() = if (modeInfo != null && modeInfo!!.modeCompat != null) {
            if (isRealTime
            ) modeInfo!!.modeCompat!!.realTimeIconRes
            else modeInfo!!.modeCompat!!.iconRes
        } else {
            0
        }

    @get:DrawableRes
    val darkVehicleIconWithNoRealtimeChecking: Int
        get() = if (modeInfo != null && modeInfo!!.modeCompat != null) {
            modeInfo!!.modeCompat!!.iconRes
        } else {
            0
        }

    fun getLightTransportIcon(context: Context): Drawable? {
        return if (modeInfo != null && modeInfo!!.modeCompat != null) {
            if (isRealTime
            ) modeInfo!!.modeCompat!!.getRealtimeMapIconRes(context)
            else modeInfo!!.modeCompat!!.getMapIconRes(context)
        } else {
            VehicleDrawables.createLightDrawable(context, R.drawable.v4_ic_map_location)
        }
    }

    val isPlane: Boolean
        get() = getType() == SCHEDULED && transportModeId != null && transportModeId!!.startsWith(
            TransportMode.ID_AIR
        )

    fun hasTimeTable(): Boolean {
        return getType() == SCHEDULED && mServiceTripId != null && !(isContinuation || isPlane)
    }


    val isWalking: Boolean
        get() = (transportModeId != null && transportModeId!!.startsWith("wa_"))

    val isCycling: Boolean
        get() = (transportModeId != null && (transportModeId!!.startsWith("bic") || transportModeId!!.startsWith(
            "cy_"
        )))

    val isWheelchair: Boolean
        get() = (transportModeId != null && transportModeId == TransportMode.ID_WHEEL_CHAIR)

    private fun getDurationWithoutTrafficText(context: Context): String {
        val durationText = getDurationInDaysHoursMins(context, durationWithoutTraffic.toInt())
        return context.resources.getString(R.string._pattern_w_slasho_traffic, durationText)
    }

    fun lineColor(): Int {
        if (isWalking) return Color.TRANSPARENT
        if (serviceColor == null) {
            if (modeInfo != null && modeInfo!!.color != null) {
                return modeInfo!!.color!!.color
            }
            return Color.TRANSPARENT
        }
        return serviceColor!!.color
    }

    fun hasCarParks(): Boolean {
        return hasCarParks
    }

    fun setHasCarParks(hasCarParks: Boolean) {
        this.hasCarParks = hasCarParks
    }

    val isQuickBooking: Boolean
        get() = booking != null &&
            (booking!!.quickBookingsUrl != null || booking!!.confirmation != null)

    fun getTurnByTurn(): TurnByTurn? = turnByTurn?.run { TurnByTurn.valueOf(this) }

    companion object {
        /**
         * FIXME: Should replace this with Quantity Strings.
         * See http://developer.android.com/intl/vi/guide/topics/resources/string-resource.html#Plurals.
         */
        fun convertStopCountToText(context: Context, stopCount: Int): String {
            return String.format(
                Locale.US,
                context.resources.getQuantityString(R.plurals.number_of_stops, stopCount),
                stopCount
            )
        }
    }
}