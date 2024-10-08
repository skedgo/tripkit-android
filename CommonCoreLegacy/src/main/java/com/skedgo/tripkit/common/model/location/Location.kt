package com.skedgo.tripkit.common.model.location

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.text.TextUtils
import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.common.util.StringUtils
import com.skedgo.tripkit.regionrouting.data.Operator
import com.skedgo.tripkit.regionrouting.data.RouteDetails
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

open class Location() : Parcelable {
    var mId: Long = 0
    var id: String = ""
    var isFavourite: Boolean = false
        protected set
    open var locationType: Int = TYPE_UNKNOWN
    var favouriteSortOrderIndex: Int = 0
    var ratingCount: Int
    var averageRating: Float
    var ratingImageUrl: String? = null
    var source: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("address")
    var address: String? = null

    @SerializedName("lat")
    var lat: Double

    @SerializedName("lng")
    var lon: Double

    @SerializedName("exact")
    var exact = false

    @SerializedName("bearing")
    var bearing: Int

    @SerializedName("region")
    var region: String? = null

    @SerializedName("phone")
    var phoneNumber: String? = null

    @SerializedName("URL")
    var url: String? = null

    @SerializedName("timezone")
    var timeZone: String? = null

    @SerializedName("popularity")
    var popularity = 0

    @SerializedName("class")
    var locationClass: String? = null

    @SerializedName("w3w")
    var w3w: String? = null

    @SerializedName("w3wInfoURL")
    var w3wInfoURL: String? = null

    @SerializedName("appUrl")
    var appUrl: String? = null

    @SerializedName("withExternalApp")
    var withExternalApp = false
    var operators: List<Operator>? = null
    val routes: List<RouteDetails>? = null

    var modeIdentifiers: List<String>? = null

    init {
        lat = ZERO_LAT
        lon = ZERO_LON
        averageRating = -1f
        ratingCount = -1
        bearing = NO_BEARING
    }

    constructor(other: Location?) : this() {
        fillFrom(other)
    }

    constructor(lat: Double, lon: Double) : this() {
        this.lat = lat
        this.lon = lon
    }

    override fun equals(o: Any?): Boolean {
        return this === o || o is Location && equalTo(o)
    }

    open fun fillFrom(other: Location?) {
        if (other == null) {
            return
        }

        mId = other.mId
        name = other.name
        address = other.address
        lat = other.lat
        lon = other.lon
        exact = other.exact
        bearing = other.bearing
        locationType = other.locationType
        isFavourite = other.isFavourite
        phoneNumber = other.phoneNumber
        url = other.url
        ratingCount = other.ratingCount
        averageRating = other.averageRating
        ratingImageUrl = other.ratingImageUrl
        source = other.source
        favouriteSortOrderIndex = other.favouriteSortOrderIndex
        popularity = other.popularity
        locationClass = other.locationClass
        w3w = other.w3w
        w3wInfoURL = other.w3wInfoURL
        appUrl = other.appUrl
        withExternalApp = other.withExternalApp
        region = other.region
    }

    fun isFavourite(favourite: Boolean) {
        isFavourite = favourite
    }

    fun hasValidCoordinates(): Boolean {
        return !(lat == 0.0 && lon == 0.0)
    }

    val coordinateString: String
        get() = "(" + round(lat) + ", " + round(lon) + ")"

    fun distanceTo(location: Location?): Int {
        return if (location == null || !location.hasValidCoordinates()) {
            -1
        } else {
            distanceTo(location.lat, location.lon)
        }
    }

    fun isApproximatelyAt(other: Location?): Boolean {
        return distanceTo(other) < APPROXIMATE_EQUALITY_METERS
    }

    fun isLooselyApproximatelyAt(other: Location?): Boolean {
        return distanceTo(other) < APPROXIMATE_EQUALITY_METERS_LOOSE
    }

    fun isApproximatelyAt(lat: Double, lon: Double): Boolean {
        return distanceTo(lat, lon) < APPROXIMATE_EQUALITY_METERS
    }

    fun equalsByLatLon(_loc: Location?): Boolean {
        return _loc != null && _loc.lat == lat && _loc.lon == lon
    }

    val isNonZeroLocation: Boolean
        get() = !(lat == ZERO_LAT && lon == ZERO_LON)

    /**
     * Get the distance between this and another point
     *
     *
     * This implementation was pulled from:
     * [CodeCodex](http://www.codecodex.com/wiki/Calculate_Distance_Between_Two_Points_on_a_Globe#Java)
     *
     * @param lat
     * @param lon
     * @return The distance in meters between `this` and `that`
     * @see [Haversine Formula](http://en.wikipedia.org/wiki/Haversine_formula)
     */
    fun distanceTo(lat: Double, lon: Double): Int {
        val dLat = Math.toRadians(lat - this.lat)
        val dLon = Math.toRadians(lon - this.lon)

        val a = sin(dLat / 2) * sin(dLat / 2) + cos(
            Math.toRadians(this.lat)
        ) * cos(Math.toRadians(lat)) * sin(dLon / 2) * sin(
            dLon / 2
        )

        val c = 2 * asin(sqrt(a))
        return (EARTH_RADIUS_IN_METERS * c).toInt()
    }

    /**
     * @param other
     * @return The bearing from this location to another other
     */
    fun getBearingTo(other: Location): Double {
        return getBearingTo(other.lat, other.lon)
    }

    /**
     * @param lat
     * @param lon
     * @return The bearing from this location to another other
     *
     *
     * Kudos: http://stackoverflow.com/a/9462757/755332
     */
    fun getBearingTo(lat: Double, lon: Double): Double {
        val longitude1 = this.lon
        val longitude2 = lon

        val latitude1 = Math.toRadians(this.lat)
        val latitude2 = Math.toRadians(lat)

        val longDiff = Math.toRadians(longitude2 - longitude1)
        val y = sin(longDiff) * cos(latitude2)
        val x = cos(latitude1) * sin(latitude2) - sin(latitude1) * cos(latitude2) * cos(longDiff)

        return (Math.toDegrees(atan2(y, x)) + 360) % 360
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeLong(mId)
        out.writeString(name)
        out.writeString(address)
        out.writeDouble(lat)
        out.writeDouble(lon)
        out.writeInt(if (exact) 1 else 0)
        out.writeInt(bearing)
        out.writeInt(locationType)
        out.writeInt(if (isFavourite) 1 else 0)
        out.writeString(phoneNumber)
        out.writeString(url)
        out.writeInt(ratingCount)
        out.writeFloat(averageRating)
        out.writeString(ratingImageUrl)
        out.writeString(source)
        out.writeInt(favouriteSortOrderIndex)
        out.writeString(timeZone)
        out.writeInt(popularity)
        out.writeString(locationClass)
        out.writeString(w3w)
        out.writeString(w3wInfoURL)
        out.writeString(appUrl)
        out.writeInt(if (withExternalApp) 1 else 0)
        out.writeString(region)
        out.writeTypedList(operators)
        out.writeTypedList(routes)
        out.writeList(modeIdentifiers)
    }

    val displayName: String
        /**
         * To present a human-readable name of a location.
         * Invoke this if we want to present a location to users
         * (e.g, a pin on a map, location of an event).
         */
        get() = if (!name.isNullOrBlank()) {
            name!!.trim { it <= ' ' }
        } else if (!address.isNullOrBlank()) {
            address!!.trim { it <= ' ' }
        } else {
            coordinateString
        }

    val displayAddress: String
        get() = if (!address.isNullOrBlank()) {
            address!!.trim { it <= ' ' }
        } else if (!name.isNullOrBlank()) {
            name!!.trim { it <= ' ' }
        } else {
            coordinateString
        }

    val nameOrApproximateAddress: String?
        /**
         * @return name (if a place got a name: station, uni, .etc) or suburb's name (if that's an address)
         */
        get() {
            if (!TextUtils.isEmpty(name)) {
                return name!!.trim { it <= ' ' }
            }

            if (!TextUtils.isEmpty(address)) {
                val parts =
                    address!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                return if (parts.size > 1) {
                    parts[1].trim { it <= ' ' }
                } else {
                    address!!.trim { it <= ' ' }
                }
            }
            return null
        }

    protected fun round(d: Double): Double {
        return Math.round(d * 10000) / 10000.0
    }

    private fun equalTo(another: Location): Boolean {
        return ((equals(name, another.name)
            && equals(
            address,
            another.address
        )) && lat == another.lat && lon == another.lon && exact == another.exact && bearing == another.bearing && equals(
            phoneNumber,
            another.phoneNumber
        )
            && equals(url, another.url)
            && equals(timeZone, another.timeZone)
            && equals(popularity, another.popularity)
            && equals(locationClass, another.locationClass)
            && equals(w3w, another.w3w)
            && equals(w3wInfoURL, another.w3wInfoURL)
            && equals(region, another.region)
            && equals(operators, another.operators)
            && equals(routes, another.routes))
    }

    override fun hashCode(): Int {
        var result = mId.hashCode()
        result = 31 * result + lat.hashCode()
        result = 31 * result + lon.hashCode()
        return result
    }

    companion object {
        /**
         * No known location type
         */
        const val TYPE_UNKNOWN: Int = -1

        /**
         * The location is a scheduled stop
         */
        const val TYPE_SCHEDULED_STOP: Int = 1

        /**
         * Location is a stop on a user's trip
         */
        const val TYPE_SERVICE_STOP: Int = 2

        /**
         * Location comes from previous search/geocoding history or long-pressed
         */
        const val TYPE_HISTORY: Int = 3

        /**
         * Location comes from users calendar
         */
        const val TYPE_CALENDAR: Int = 4

        /**
         * Location comes from a contact in the users address book
         */
        const val TYPE_CONTACT: Int = 5

        /**
         * Location is info from the users personal contact card (home/work address etc)
         */
        const val TYPE_PERSONAL: Int = 6

        /**
         * Location is info from the users personal contact card (home/work address etc)
         */
        const val TYPE_HOME: Int = 7 //so we never delete this location
        const val TYPE_WORK: Int = 8

        const val TYPE_CURRENT_LOCATION: Int =
            9 // Doesn't actually resolve to anything, but makes life easier for keeping track

        const val TYPE_E_BIKE: Int = 10 //Neuron or Lime

        /**
         * What3Words type
         */
        const val TYPE_W3W: Int = 9

        const val TYPE_SCHOOL: Int = 11

        const val NO_BEARING: Int = Int.MAX_VALUE
        const val ZERO_LAT: Double = 0.0
        const val ZERO_LON: Double = 0.0

        /**
         * Source
         */
        const val TRIPGO: String = "tripgo"
        const val LOCAL: String = "local"
        const val GOOGLE: String = "google"
        const val FOURSQUARE: String = "foursquare"
        @JvmField
        val CREATOR: Creator<Location> = object : Creator<Location> {
            override fun createFromParcel(`in`: Parcel): Location {
                val location = Location()

                location.mId = `in`.readLong()
                location.name = `in`.readString()
                location.address = `in`.readString()
                location.lat = `in`.readDouble()
                location.lon = `in`.readDouble()
                location.exact = `in`.readInt() == 1
                location.bearing = `in`.readInt()
                location.locationType = `in`.readInt()
                location.isFavourite = `in`.readInt() == 1
                location.phoneNumber = `in`.readString()
                location.url = `in`.readString()
                location.ratingCount = `in`.readInt()
                location.averageRating = `in`.readFloat()
                location.ratingImageUrl = `in`.readString()
                location.source = `in`.readString()
                location.favouriteSortOrderIndex = `in`.readInt()
                location.timeZone = `in`.readString()
                location.popularity = `in`.readInt()
                location.locationClass = `in`.readString()
                location.w3w = `in`.readString()
                location.w3wInfoURL = `in`.readString()
                location.appUrl = `in`.readString()
                location.withExternalApp = `in`.readInt() == 1
                location.region = `in`.readString()

                val operators: List<Operator> = ArrayList()
                `in`.readTypedList(operators, Operator.CREATOR)
                location.operators = operators

                val routes: List<RouteDetails> = ArrayList()
                `in`.readTypedList(routes, RouteDetails.CREATOR)

                val modeIdentifiers: List<String> = ArrayList()
                `in`.readList(modeIdentifiers, String::class.java.classLoader)
                location.modeIdentifiers = modeIdentifiers

                return location
            }

            override fun newArray(size: Int): Array<Location?> {
                return arrayOfNulls(size)
            }
        }
        private const val EARTH_RADIUS_IN_METERS = 6371 * 1000

        /**
         * Locations this close to each other will be considered equal
         * for the sake of comparing 2 locations
         */
        private const val APPROXIMATE_EQUALITY_METERS = 30

        /**
         * relax the diameter
         */
        private const val APPROXIMATE_EQUALITY_METERS_LOOSE = 60

        fun isValidLocation(loc: Location?): Boolean {
            return (loc != null) && loc.isNonZeroLocation
        }

        private fun equals(left: Any?, right: Any?): Boolean {
            return left === right || (left != null && left == right)
        }
    }
}