package com.skedgo.tripkit.routing;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;
import com.skedgo.tripkit.common.R;
import com.skedgo.tripkit.common.model.RealtimeAlert;
import com.skedgo.tripkit.common.util.TripSegmentListResolver;

import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class RoutingResponse {
    public static final String FORMAT_DIRECTION = "Direction: %s";
    public static final Integer ERROR_CODE_NO_FROM_LOCATION = 1102;

    /**
     * This is used for parsing saved trip from shared url.
     */
    @SerializedName("region")
    private String mRegionName;

    @SerializedName("usererror")
    private boolean mHasUserError;

    @SerializedName("error")
    private String mErrorMessage;

    @SerializedName("errorCode")
    private Integer mErrorCode;

    @SerializedName("segmentTemplates")
    private ArrayList<JsonObject> segmentTemplates;

    @SerializedName("groups")
    private ArrayList<TripGroup> mTripGroupList;

    @SerializedName("alerts")
    private ArrayList<RealtimeAlert> alerts;

    private transient TripSegmentListResolver mTripSegmentListResolver;
    private transient Map<Long, RealtimeAlert> alertCache;

    /**
     * Replaces '<DIRECTION>' with segment's 'serviceDirection'
     *
     * @param serviceDirectionNode The Json node that contains value for 'serviceDirection'
     * @param notes                The 'notes' text that contains the '<DIRECTION>' template
     * @return The 'notes' text that has been processed
     */
    public static String processDirectionTemplate(JsonPrimitive serviceDirectionNode, String notes, Resources resources) {
        // Ignore processing if the 'notes' text is empty
        if (TextUtils.isEmpty(notes)) {
            return notes;
        }

        if (!isElementMissing(serviceDirectionNode)) {
            String serviceDirection = serviceDirectionNode.getAsString();
            if (!TextUtils.isEmpty(serviceDirection)) {
                notes = notes.replace(SegmentNotesTemplates.TEMPLATE_DIRECTION, String.format(getFormatDirection(resources), serviceDirection));
            } else {
                // The 'serviceDirection' node is empty, clear the template.
                notes = notes.replace(SegmentNotesTemplates.TEMPLATE_DIRECTION, "");
            }
        } else {
            // The 'serviceDirection' node doesn't exist, clear the template.
            notes = notes.replace(SegmentNotesTemplates.TEMPLATE_DIRECTION, "");
        }

        return notes;
    }

    private static String getFormatDirection(Resources resources) {
        if (resources == null) {
            return FORMAT_DIRECTION;
        }
        return resources.getString(R.string.direction) + ": %s";
    }

    private static boolean isElementMissing(JsonElement element) {
        return (element == null) || element.isJsonNull();
    }

    public String getRegionName() {
        return mRegionName;
    }

    public boolean hasError() {
        return mHasUserError;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public Integer getErrorCode() {
        return mErrorCode;
    }

    public ArrayList<TripGroup> getTripGroupList() {
        return mTripGroupList;
    }

    public void processRawData(Resources resources, Gson gson) {
        SparseArray<JsonObject> segmentTemplateMap = createSegmentTemplateMap(segmentTemplates);
        mTripGroupList = processRawData(resources, gson, mTripGroupList, segmentTemplateMap);
    }

    public ArrayList<TripGroup> processRawData(Resources resources, Gson gson,
                                               ArrayList<TripGroup> tripGroups,
                                               SparseArray<JsonObject> segmentTemplateMap) {
        if (CollectionUtils.isEmpty(tripGroups)) {
            return tripGroups;
        }

        if (CollectionUtils.isNotEmpty(alerts)) {
            alertCache = new HashMap<>(alerts.size());
            for (RealtimeAlert alert : alerts) {
                alertCache.put(alert.remoteHashCode(), alert);
            }
        }
        for (TripGroup tripGroup : tripGroups) {
            ArrayList<Trip> trips = tripGroup.getTrips();
            if (CollectionUtils.isEmpty(trips)) {
                continue;
            }
            for (Trip trip : trips) {
                trip.setGroup(tripGroup);

                ArrayList<JsonObject> rawSegments = trip.rawSegmentList;
                trip.rawSegmentList = null;
                if (CollectionUtils.isEmpty(rawSegments)) {
                    continue;
                }

                ArrayList<TripSegment> segments = createSegmentsFromTemplate(
                        gson,
                        segmentTemplateMap,
                        rawSegments,
                        resources
                );
                trip.setSegments(segments);
                processTripSegmentRealTimeVehicle(segments);
            }

            Collections.sort(trips, TripComparators.TIME_COMPARATOR_CHAIN);
        }

        if (mTripSegmentListResolver == null) {
            mTripSegmentListResolver = new TripSegmentListResolver(resources);
        }

        resolveTripGroupList(tripGroups);
        return tripGroups;
    }

    public SparseArray<JsonObject> createSegmentTemplateMap(List<JsonObject> segmentTemplates) {
        SparseArray<JsonObject> segmentTemplateMap = new SparseArray<JsonObject>();
        if (CollectionUtils.isNotEmpty(segmentTemplates)) {
            for (JsonObject segmentTemplate : segmentTemplates) {
                if (segmentTemplate != null) {
                    JsonPrimitive hashCodeNode = segmentTemplate.getAsJsonPrimitive(SegmentJsonKeys.NODE_HASH_CODE);
                    if (isElementMissing(hashCodeNode)) {
                        continue;
                    }

                    int hashCode = hashCodeNode.getAsInt();
                    segmentTemplateMap.put(hashCode, segmentTemplate);
                }
            }
        }

        return segmentTemplateMap;
    }

    public ArrayList<RealtimeAlert> getAlerts() {
        return alerts;
    }

    private void resolveTripGroupList(ArrayList<TripGroup> tripGroupList) {
        for (TripGroup tripGroup : tripGroupList) {
            if (CollectionUtils.isNotEmpty(tripGroup.getTrips())) {
                for (Trip trip : tripGroup.getTrips()) {
                    if (trip != null) {
                        mTripSegmentListResolver
                                .setOrigin(trip.getFrom())
                                .setDestination(trip.getTo())
                                .setTripSegmentList(trip.getSegments())
                                .resolve();
                    }
                }
            }
        }
    }

    private void processTripSegmentRealTimeVehicle(ArrayList<TripSegment> tripSegmentList) {
        for (TripSegment tripSegment : tripSegmentList) {
            RealTimeVehicle vehicle = tripSegment.getRealTimeVehicle();
            if (vehicle == null) {
                continue;
            }

            vehicle.setServiceTripId(tripSegment.getServiceTripId());
            vehicle.setStartStopCode(tripSegment.getStartStopCode());
            vehicle.setEndStopCode(tripSegment.getEndStopCode());

            tripSegment.setRealTimeVehicle(vehicle);
        }
    }

    private ArrayList<TripSegment> createSegmentsFromTemplate(Gson gson,
                                                              SparseArray<JsonObject> segmentTemplateMap,
                                                              ArrayList<JsonObject> rawSegments,
                                                              Resources resources) {
        ArrayList<TripSegment> segments = new ArrayList<TripSegment>(rawSegments.size());
        for (JsonObject rawSegment : rawSegments) {

            if (rawSegment == null) {
                continue;
            }

            JsonPrimitive hashCodeNode = rawSegment.getAsJsonPrimitive(SegmentJsonKeys.NODE_SEGMENT_TEMPLATE_HASH_CODE);
            if (isElementMissing(hashCodeNode)) {
                continue;
            }

            int hashCode = hashCodeNode.getAsInt();
            JsonObject segmentTemplate = segmentTemplateMap.get(hashCode);
            TripSegment segment = createSegmentFromTemplate(gson, rawSegment, segmentTemplate, resources);
            segments.add(segment);
        }

        return segments;
    }

    private TripSegment createSegmentFromTemplate(Gson gson,
                                                  JsonObject rawSegment,
                                                  JsonObject segmentTemplate,
                                                  Resources resources) {
        if (segmentTemplate != null) {
            Set<Map.Entry<String, JsonElement>> entrySet = segmentTemplate.entrySet();
            for (Map.Entry<String, JsonElement> entry : entrySet) {
                String key = entry.getKey();
                JsonElement value = entry.getValue();

                if (SegmentJsonKeys.NODE_ACTION.equals(key)) {
                    processSegmentTemplateAction(rawSegment, value);
                } else if (SegmentJsonKeys.NODE_NOTES.equals(key)) {
                    processSegmentTemplateNotes(rawSegment, value, resources);
                } else {
                    rawSegment.add(key, value);
                }
            }
        }
        TripSegment segment = new TripSegment();
        try {
            segment = gson.fromJson(rawSegment, TripSegment.class);
        } catch (Exception e) {
            Log.e("TripKit", "GSON ERROR", e);
            Log.e("TripKit", rawSegment.toString());
        }
        if (segment.getAlertHashCodes() != null && alertCache != null) {
            ArrayList<RealtimeAlert> segmentAlerts = null;
            for (long alertHashCode : segment.getAlertHashCodes()) {
                RealtimeAlert alert = alertCache.get(alertHashCode);
                if (alert != null) {
                    // Lazily initialize.
                    if (segmentAlerts == null) {
                        segmentAlerts = new ArrayList<>();
                    }

                    segmentAlerts.add(alert);
                }
            }

            if (segmentAlerts != null) {
                segment.setAlerts(segmentAlerts);
            }
        }
        return segment;
    }

    private void processSegmentTemplateNotes(JsonObject rawSegment, JsonElement notesNode, Resources resources) {
        if (isElementMissing(notesNode)) {
            return;
        }

        String notes = notesNode.getAsString();
        if (!TextUtils.isEmpty(notes)) {
            JsonPrimitive serviceNameNode = rawSegment.getAsJsonPrimitive(SegmentJsonKeys.NODE_SERVICE_NAME);
            if (!isElementMissing(serviceNameNode)) {
                final String serviceName = serviceNameNode.getAsString();
                if (serviceName != null) {
                    // No need to check empty. Reason: https://redmine.buzzhives.com/issues/5803.
                    notes = notes.replace(SegmentNotesTemplates.TEMPLATE_LINE_NAME, serviceName);
                }
            }

            // Replaces '<DIRECTION>' with segment's 'serviceDirection'
            notes = processDirectionTemplate(rawSegment.getAsJsonPrimitive(SegmentJsonKeys.NODE_SERVICE_DIRECTION), notes, resources);

            rawSegment.addProperty(SegmentJsonKeys.NODE_NOTES, notes);
        }
    }

    private void processSegmentTemplateAction(JsonObject rawSegment, JsonElement actionNode) {
        if (isElementMissing(actionNode)) {
            return;
        }

        String action = actionNode.getAsString();
        if (!TextUtils.isEmpty(action)) {
            action = processActionNumber(rawSegment, action);
            rawSegment.addProperty(SegmentJsonKeys.NODE_ACTION, action);
        }
    }

    private String processActionNumber(JsonObject rawSegment, String action) {
        JsonPrimitive serviceNumberNode = rawSegment.getAsJsonPrimitive(SegmentJsonKeys.NODE_SERVICE_NUMBER);
        if (!isElementMissing(serviceNumberNode)) {
            String serviceNumber = serviceNumberNode.getAsString();
            if (!TextUtils.isEmpty(serviceNumber)) {
                action = action.replace(SegmentActionTemplates.TEMPLATE_NUMBER, serviceNumber);
            } else if (action.contains(SegmentActionTemplates.TEMPLATE_NUMBER)) {
                // Doesn't have service number but the template still exists.
                // We'll replace it with mode value instead.
                // 'Take <NUMBER>' will then become 'Take the bus' if the mode is 'bus'.
                JsonPrimitive modeNode = rawSegment.getAsJsonPrimitive("mode");
                if (!isElementMissing(modeNode)) {
                    String mode = modeNode.getAsString();
                    if (!TextUtils.isEmpty(mode)) {
                        action = action.replace(SegmentActionTemplates.TEMPLATE_NUMBER, "the " + mode);
                    }

                    // Else? if it takes place, call backend guys right away.
                }
            }
        }

        return action;
    }
}