package com.skedgo.tripkit.routing

import android.content.res.Resources
import android.text.TextUtils
import android.util.Log
import android.util.SparseArray
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.common.R
import com.skedgo.tripkit.common.model.realtimealert.RealtimeAlert
import com.skedgo.tripkit.common.util.TripSegmentListResolver
import org.apache.commons.collections4.CollectionUtils
import java.util.Collections

class RoutingResponse {
    /**
     * This is used for parsing saved trip from shared url.
     */
    @SerializedName("region")
    val regionName: String? = null

    @SerializedName("usererror")
    private val mHasUserError = false

    @SerializedName("error")
    val errorMessage: String? = null

    @SerializedName("errorCode")
    val errorCode: Int? = null

    @SerializedName("segmentTemplates")
    private val segmentTemplates: ArrayList<JsonObject>? = null

    @SerializedName("groups")
    var tripGroupList: ArrayList<TripGroup>? = null
        private set

    @JvmField
    @SerializedName("alerts")
    val alerts: ArrayList<RealtimeAlert>? = null

    @Transient
    private var mTripSegmentListResolver: TripSegmentListResolver? = null

    @Transient
    private var alertCache: HashMap<Long, RealtimeAlert>? = null

    fun hasError(): Boolean {
        return mHasUserError
    }

    fun processRawData(resources: Resources?, gson: Gson) {
        val segmentTemplateMap = createSegmentTemplateMap(segmentTemplates)
        tripGroupList = processRawData(resources, gson, tripGroupList, segmentTemplateMap)
    }

    fun processRawData(
        resources: Resources?, gson: Gson,
        tripGroups: ArrayList<TripGroup>?,
        segmentTemplateMap: SparseArray<JsonObject>
    ): ArrayList<TripGroup>? {
        if (CollectionUtils.isEmpty(tripGroups)) {
            return tripGroups
        }

        if (CollectionUtils.isNotEmpty(alerts)) {
            alertCache = HashMap(alerts!!.size)
            for (alert in alerts) {
                alertCache!![alert!!.remoteHashCode()] = alert
            }
        }
        for (tripGroup in tripGroups!!) {
            val trips: ArrayList<Trip>? = tripGroup!!.trips
            if (CollectionUtils.isEmpty(trips)) {
                continue
            }
            for (trip in trips!!) {
                trip!!.group = tripGroup

                val rawSegments: ArrayList<JsonObject>? = trip.rawSegmentList
                trip.rawSegmentList = null
                if (CollectionUtils.isEmpty(rawSegments)) {
                    continue
                }

                val segments = createSegmentsFromTemplate(
                    gson,
                    segmentTemplateMap,
                    rawSegments,
                    resources
                )
                trip.segmentList = segments
                processTripSegmentRealTimeVehicle(segments)
            }

            Collections.sort(trips, TripComparators.TIME_COMPARATOR_CHAIN)
        }

        if (mTripSegmentListResolver == null) {
            mTripSegmentListResolver = TripSegmentListResolver(resources!!)
        }

        resolveTripGroupList(tripGroups)
        return tripGroups
    }

    fun createSegmentTemplateMap(segmentTemplates: List<JsonObject>?): SparseArray<JsonObject> {
        val segmentTemplateMap = SparseArray<JsonObject>()
        if (CollectionUtils.isNotEmpty(segmentTemplates)) {
            for (segmentTemplate in segmentTemplates!!) {
                if (segmentTemplate != null) {
                    val hashCodeNode =
                        segmentTemplate.getAsJsonPrimitive(SegmentJsonKeys.NODE_HASH_CODE)
                    if (isElementMissing(hashCodeNode)) {
                        continue
                    }

                    val hashCode = hashCodeNode.asInt
                    segmentTemplateMap.put(hashCode, segmentTemplate)
                }
            }
        }

        return segmentTemplateMap
    }

    private fun resolveTripGroupList(tripGroupList: ArrayList<TripGroup>?) {
        for (tripGroup in tripGroupList.orEmpty()) {
            if (CollectionUtils.isNotEmpty(tripGroup.trips)) {
                for (trip in tripGroup.trips.orEmpty()) {
                    mTripSegmentListResolver
                        ?.setOrigin(trip.from)
                        ?.setDestination(trip.to)
                        ?.setTripSegmentList(trip.segmentList)
                        ?.resolve()
                }
            }
        }
    }

    private fun processTripSegmentRealTimeVehicle(tripSegmentList: ArrayList<TripSegment>) {
        for (tripSegment in tripSegmentList) {
            val vehicle = tripSegment.realTimeVehicle ?: continue

            vehicle.serviceTripId = tripSegment.serviceTripId
            vehicle.startStopCode = tripSegment.startStopCode
            vehicle.endStopCode = tripSegment.endStopCode

            tripSegment.realTimeVehicle = vehicle
        }
    }

    private fun createSegmentsFromTemplate(
        gson: Gson,
        segmentTemplateMap: SparseArray<JsonObject>,
        rawSegments: ArrayList<JsonObject>?,
        resources: Resources?
    ): ArrayList<TripSegment> {
        val segments = ArrayList<TripSegment>(
            rawSegments!!.size
        )
        for (rawSegment in rawSegments) {
            if (rawSegment == null) {
                continue
            }

            val hashCodeNode =
                rawSegment.getAsJsonPrimitive(SegmentJsonKeys.NODE_SEGMENT_TEMPLATE_HASH_CODE)
            if (isElementMissing(hashCodeNode)) {
                continue
            }

            val hashCode = hashCodeNode.asInt
            val segmentTemplate = segmentTemplateMap[hashCode]
            val segment = createSegmentFromTemplate(gson, rawSegment, segmentTemplate, resources)
            segments.add(segment)
        }

        return segments
    }

    private fun createSegmentFromTemplate(
        gson: Gson,
        rawSegment: JsonObject,
        segmentTemplate: JsonObject?,
        resources: Resources?
    ): TripSegment {
        if (segmentTemplate != null) {
            val entrySet = segmentTemplate.entrySet()
            for ((key, value) in entrySet) {
                if (SegmentJsonKeys.NODE_ACTION == key) {
                    processSegmentTemplateAction(rawSegment, value)
                } else if (SegmentJsonKeys.NODE_NOTES == key) {
                    processSegmentTemplateNotes(rawSegment, value, resources)
                } else {
                    rawSegment.add(key, value)
                }
            }
        }
        var segment = TripSegment()
        try {
            segment = gson.fromJson(rawSegment, TripSegment::class.java)
        } catch (e: Exception) {
            Log.e("TripKit", "GSON ERROR", e)
            Log.e("TripKit", rawSegment.toString())
        }
        if (segment.alertHashCodes != null && alertCache != null) {
            var segmentAlerts: ArrayList<RealtimeAlert>? = null
            for (alertHashCode in segment.alertHashCodes) {
                val alert = alertCache!![alertHashCode]
                if (alert != null) {
                    // Lazily initialize.
                    if (segmentAlerts == null) {
                        segmentAlerts = ArrayList()
                    }

                    segmentAlerts.add(alert)
                }
            }

            if (segmentAlerts != null) {
                segment.alerts = segmentAlerts
            }
        }
        return segment
    }

    private fun processSegmentTemplateNotes(
        rawSegment: JsonObject,
        notesNode: JsonElement,
        resources: Resources?
    ) {
        if (isElementMissing(notesNode)) {
            return
        }

        var notes = notesNode.asString
        if (!TextUtils.isEmpty(notes)) {
            val serviceNameNode = rawSegment.getAsJsonPrimitive(SegmentJsonKeys.NODE_SERVICE_NAME)
            if (!isElementMissing(serviceNameNode)) {
                val serviceName = serviceNameNode.asString
                if (serviceName != null) {
                    // No need to check empty. Reason: https://redmine.buzzhives.com/issues/5803.
                    notes = notes.replace(SegmentNotesTemplates.TEMPLATE_LINE_NAME, serviceName)
                }
            }

            if(rawSegment.has(SegmentJsonKeys.NODE_SERVICE_DIRECTION)) {
                // Replaces '<DIRECTION>' with segment's 'serviceDirection'
                notes = processDirectionTemplate(
                    rawSegment.getAsJsonPrimitive(SegmentJsonKeys.NODE_SERVICE_DIRECTION),
                    notes,
                    resources
                )

                rawSegment.addProperty(SegmentJsonKeys.NODE_NOTES, notes)
            }
        }
    }

    private fun processSegmentTemplateAction(rawSegment: JsonObject, actionNode: JsonElement) {
        if (isElementMissing(actionNode)) {
            return
        }

        var action = actionNode.asString
        if (!TextUtils.isEmpty(action)) {
            action = processActionNumber(rawSegment, action)
            rawSegment.addProperty(SegmentJsonKeys.NODE_ACTION, action)
        }
    }

    private fun processActionNumber(rawSegment: JsonObject, action: String): String {
        var action = action
        val serviceNumberNode = rawSegment.getAsJsonPrimitive(SegmentJsonKeys.NODE_SERVICE_NUMBER)
        if (!isElementMissing(serviceNumberNode)) {
            val serviceNumber = serviceNumberNode.asString
            if (!TextUtils.isEmpty(serviceNumber)) {
                action = action.replace(SegmentActionTemplates.TEMPLATE_NUMBER, serviceNumber)
            } else if (action.contains(SegmentActionTemplates.TEMPLATE_NUMBER)) {
                // Doesn't have service number but the template still exists.
                // We'll replace it with mode value instead.
                // 'Take <NUMBER>' will then become 'Take the bus' if the mode is 'bus'.
                val modeNode = rawSegment.getAsJsonPrimitive("mode")
                if (!isElementMissing(modeNode)) {
                    val mode = modeNode.asString
                    if (!TextUtils.isEmpty(mode)) {
                        action = action.replace(SegmentActionTemplates.TEMPLATE_NUMBER, "the $mode")
                    }

                    // Else? if it takes place, call backend guys right away.
                }
            }
        }

        return action
    }

    companion object {
        const val FORMAT_DIRECTION: String = "Direction: %s"
        const val ERROR_CODE_NO_FROM_LOCATION: Int = 1102

        /**
         * Replaces '<DIRECTION>' with segment's 'serviceDirection'
         *
         * @param serviceDirectionNode The Json node that contains value for 'serviceDirection'
         * @param notes                The 'notes' text that contains the '<DIRECTION>' template
         * @return The 'notes' text that has been processed
        </DIRECTION></DIRECTION> */
        @JvmStatic
        fun processDirectionTemplate(
            serviceDirectionNode: JsonPrimitive,
            notes: String,
            resources: Resources?
        ): String {
            // Ignore processing if the 'notes' text is empty
            var notes = notes
            if (TextUtils.isEmpty(notes)) {
                return notes
            }

            if (!isElementMissing(serviceDirectionNode)) {
                val serviceDirection = serviceDirectionNode.asString
                notes = if (!TextUtils.isEmpty(serviceDirection)) {
                    notes.replace(
                        SegmentNotesTemplates.TEMPLATE_DIRECTION,
                        String.format(
                            getFormatDirection(resources),
                            serviceDirection
                        )
                    )
                } else {
                    // The 'serviceDirection' node is empty, clear the template.
                    notes.replace(SegmentNotesTemplates.TEMPLATE_DIRECTION, "")
                }
            } else {
                // The 'serviceDirection' node doesn't exist, clear the template.
                notes = notes.replace(SegmentNotesTemplates.TEMPLATE_DIRECTION, "")
            }

            return notes
        }

        private fun getFormatDirection(resources: Resources?): String {
            if (resources == null) {
                return FORMAT_DIRECTION
            }
            return resources.getString(R.string.direction) + ": %s"
        }

        private fun isElementMissing(element: JsonElement?): Boolean {
            return (element == null) || element.isJsonNull
        }
    }
}