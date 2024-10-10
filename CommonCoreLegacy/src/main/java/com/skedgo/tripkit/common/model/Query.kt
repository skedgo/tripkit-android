package com.skedgo.tripkit.common.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.text.TextUtils
import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.common.model.region.Region
import com.skedgo.tripkit.common.model.time.TimeTag
import java.util.UUID
import java.util.concurrent.TimeUnit.SECONDS

/**
 * Represents a query to find routes from A to B.
 *
 *
 * Note that, to avoid `TransactionTooLargeException`, it's discouraged to
 * pass any instance of `Query` to an `Intent` or a `Bundle`.
 * The `Parcelable` is subject to deletion at anytime.
 */
class Query : Parcelable {
    private var uuid: String = UUID.randomUUID().toString()
    var transportModeIds: MutableList<String> = ArrayList()
    private var mUnit: String? = null
    var fromLocation: Location? = null
    var toLocation: Location? = null

    /**
     * This method is deprecated. Region will be chosen using the from and to location.
     *
     * @param region
     */
    @get:Deprecated("")
    @set:Deprecated("")
    var region: Region? = null
    var timeTag: TimeTag? = null
    private var mTimeWeight = 0
    private var mBudgetWeight = 0
    private var mHassleWeight = 0
    private var mEnvironmentWeight = 0
    var cyclingSpeed: Int = 0
    var walkingSpeed: Int = 0
    var transferTime: Int = 0
    var excludedStopCodes: List<String> = ArrayList()


    private var mUseWheelchair = false

    /**
     * This is only used for XUM project. TripGo may not need it.
     * See more: https://www.flowdock.com/app/skedgo/androiddev/threads/nZJbtLU0jgsgziQpuoqhcaB-U9A.
     */
    var maxWalkingTime = 0

    fun clone(cloneTransportMode: Boolean = false): Query {
        val query = Query()
        query.fromLocation = fromLocation
        query.toLocation = toLocation
        query.timeTag = timeTag
        query.mTimeWeight = mTimeWeight
        query.mBudgetWeight = mBudgetWeight
        query.mHassleWeight = mHassleWeight
        query.mEnvironmentWeight = mEnvironmentWeight
        query.mUnit = mUnit
        query.transferTime = transferTime
        query.cyclingSpeed = cyclingSpeed
        query.walkingSpeed = walkingSpeed
        query.region = region
        query.maxWalkingTime = maxWalkingTime
        query.mUseWheelchair = mUseWheelchair

        // Perform deep copy of modes, so that removing member of one list doesn't affect the other.
        if (cloneTransportMode) {
            val cloneTransportModeIds: MutableList<String> = ArrayList(transportModeIds.size)
            cloneTransportModeIds.addAll(transportModeIds)
            query.transportModeIds = cloneTransportModeIds
        }

        // clone excludedStopCodes
        val cloneExcludedStopCodes: MutableList<String> = ArrayList(excludedStopCodes.size)
        cloneExcludedStopCodes.addAll(excludedStopCodes)
        query.excludedStopCodes = cloneExcludedStopCodes

        return query
    }

    var unit: String?
        /**
         * @return values of [Units](/tripkit-android/com.skedgo.android.common.model/-units/).
         */
        get() {
            if (TextUtils.isEmpty(mUnit)) {
                mUnit = Units.UNIT_AUTO
            }
            return mUnit
        }
        /**
         * @param unit Must be values of [Units](/tripkit-android/com.skedgo.android.common.model/-units/).
         */
        set(unit) {
            this.mUnit = unit
            if (TextUtils.isEmpty(this.mUnit)) {
                this.mUnit = Units.UNIT_AUTO
            }
        }

    fun originIsCurrentLocation(): Boolean {
        return fromLocation == null
    }

    fun destinationIsCurrentLocation(): Boolean {
        return toLocation == null
    }

    val departAfter: Long
        /**
         * @return Time in secs.
         */
        get() = if (timeTag != null && timeTag!!.type == TimeTag.TIME_TYPE_LEAVE_AFTER) {
            SECONDS.toMillis(timeTag!!.timeInSecs)
        } else {
            -1
        }

    val arriveBy: Long
        /**
         * @return Time in secs.
         */
        get() = if (timeTag != null && timeTag!!.type == TimeTag.TIME_TYPE_ARRIVE_BY) {
            SECONDS.toMillis(timeTag!!.timeInSecs)
        } else {
            -1
        }

    var timeWeight: Int
        get() = mTimeWeight
        set(weight) {
            var weight = weight
            if (weight < 0) {
                weight = 0
            } else if (weight > 100) {
                weight = 100
            }

            this.mTimeWeight = weight
        }

    var budgetWeight: Int
        get() = mBudgetWeight
        set(weight) {
            var weight = weight
            if (weight < 0) {
                weight = 0
            } else if (weight > 100) {
                weight = 100
            }

            this.mBudgetWeight = weight
        }

    var hassleWeight: Int
        get() = mHassleWeight
        set(weight) {
            var weight = weight
            if (weight < 0) {
                weight = 0
            } else if (weight > 100) {
                weight = 100
            }

            this.mHassleWeight = weight
        }

    var environmentWeight: Int
        get() = mEnvironmentWeight
        set(weight) {
            var weight = weight
            if (weight < 0) {
                weight = 0
            } else if (weight > 100) {
                weight = 100
            }

            this.mEnvironmentWeight = weight
        }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }

        if (o == null || javaClass != o.javaClass) {
            return false
        }

        val query = o as Query
        return TextUtils.equals(uuid, query.uuid)
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(uuid)
        dest.writeString(mUnit)
        dest.writeParcelable(fromLocation, 0)
        dest.writeParcelable(toLocation, 0)
        dest.writeParcelable(timeTag, 0)
        dest.writeInt(mTimeWeight)
        dest.writeInt(mBudgetWeight)
        dest.writeInt(mHassleWeight)
        dest.writeInt(mEnvironmentWeight)
        dest.writeParcelable(region, 0)
        dest.writeInt(transferTime)
        dest.writeInt(cyclingSpeed)
        dest.writeInt(walkingSpeed)
        dest.writeStringList(transportModeIds)
        dest.writeInt(maxWalkingTime)
        dest.writeStringList(excludedStopCodes)
        dest.writeInt(if (mUseWheelchair) 1 else 0)
    }

    fun uuid(): String {
        return uuid
    }

    fun useWheelchair(): Boolean {
        return mUseWheelchair
    }

    fun setUseWheelchair(useWheelchair: Boolean) {
        mUseWheelchair = useWheelchair
    }

    private fun readTransportModeIds(`in`: Parcel): List<String> {
        val ids: List<String> = ArrayList()
        `in`.readStringList(ids)
        return ids
    }

    companion object {
        @JvmField
        val CREATOR: Creator<Query> = object : Creator<Query> {
            override fun createFromParcel(`in`: Parcel): Query {
                val query = Query()
                query.uuid = `in`.readString().orEmpty()
                query.mUnit = `in`.readString()
                query.fromLocation = `in`.readParcelable(
                    Location::class.java.classLoader
                )
                query.toLocation = `in`.readParcelable(
                    Location::class.java.classLoader
                )
                query.timeTag = `in`.readParcelable(TimeTag::class.java.classLoader)

                query.mTimeWeight = `in`.readInt()
                query.mBudgetWeight = `in`.readInt()
                query.mHassleWeight = `in`.readInt()
                query.mEnvironmentWeight = `in`.readInt()
                query.region = `in`.readParcelable(
                    Region::class.java.classLoader
                )
                query.transferTime = `in`.readInt()
                query.cyclingSpeed = `in`.readInt()
                query.walkingSpeed = `in`.readInt()
                query.transportModeIds = query.readTransportModeIds(`in`).toMutableList()
                query.maxWalkingTime = `in`.readInt()

                val stops: List<String> = ArrayList()
                `in`.readStringList(stops)
                query.excludedStopCodes = stops

                query.mUseWheelchair = `in`.readInt() != 0
                return query
            }

            override fun newArray(size: Int): Array<Query?> {
                return arrayOfNulls(size)
            }
        }
    }
}