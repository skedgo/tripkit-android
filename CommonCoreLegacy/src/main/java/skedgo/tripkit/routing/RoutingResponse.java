package skedgo.tripkit.routing;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;
import com.skedgo.android.common.model.RealtimeAlert;
import com.skedgo.android.common.util.TripSegmentListResolver;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RoutingResponse {
  public static final String TEMPLATE_DIRECTION = "<DIRECTION>";
  public static final String TEMPLATE_LINE_NAME = "<LINE_NAME>";
  public static final String TEMPLATE_NUMBER = "<NUMBER>";

  public static final String NODE_SERVICE_DIRECTION = "serviceDirection";
  public static final String NODE_SERVICE_NUMBER = "serviceNumber";
  public static final String NODE_ACTION = "action";
  public static final String NODE_NOTES = "notes";
  public static final String NODE_SHAPES = "shapes";
  public static final String NODE_STOPS = "stops";
  public static final String NODE_TRAVELLED = "travelled";
  public static final String NODE_SERVICE_NAME = "serviceName";
  public static final String NODE_HASH_CODE = "hashCode";
  public static final String NODE_SEGMENT_TEMPLATE_HASH_CODE = "segmentTemplateHashCode";
  public static final String FORMAT_DIRECTION = "Direction: %s";

  /**
   * This is used for parsing saved trip from shared url.
   */
  @SerializedName("region")
  private String mRegionName;

  @SerializedName("usererror")
  private boolean mHasUserError;

  @SerializedName("error")
  private String mErrorMessage;

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
  public static String processDirectionTemplate(JsonPrimitive serviceDirectionNode, String notes) {
    // Ignore processing if the 'notes' text is empty
    if (TextUtils.isEmpty(notes)) {
      return notes;
    }

    if (!isElementMissing(serviceDirectionNode)) {
      String serviceDirection = serviceDirectionNode.getAsString();
      if (!TextUtils.isEmpty(serviceDirection)) {
        notes = notes.replace(TEMPLATE_DIRECTION, String.format(FORMAT_DIRECTION, serviceDirection));
      } else {
        // The 'serviceDirection' node is empty, clear the template.
        notes = notes.replace(TEMPLATE_DIRECTION, "");
      }
    } else {
      // The 'serviceDirection' node doesn't exist, clear the template.
      notes = notes.replace(TEMPLATE_DIRECTION, "");
    }

    return notes;
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
            rawSegments
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

  public SparseArray<JsonObject> createSegmentTemplateMap() {
    return createSegmentTemplateMap(segmentTemplates);
  }

  public SparseArray<JsonObject> createSegmentTemplateMap(ArrayList<JsonObject> segmentTemplates) {
    SparseArray<JsonObject> segmentTemplateMap = new SparseArray<JsonObject>();
    if (CollectionUtils.isNotEmpty(segmentTemplates)) {
      for (JsonObject segmentTemplate : segmentTemplates) {
        if (segmentTemplate != null) {
          JsonPrimitive hashCodeNode = segmentTemplate.getAsJsonPrimitive(NODE_HASH_CODE);
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
                                                            ArrayList<JsonObject> rawSegments) {
    ArrayList<TripSegment> segments = new ArrayList<TripSegment>(rawSegments.size());
    for (JsonObject rawSegment : rawSegments) {
      if (rawSegment == null) {
        continue;
      }

      JsonPrimitive hashCodeNode = rawSegment.getAsJsonPrimitive(NODE_SEGMENT_TEMPLATE_HASH_CODE);
      if (isElementMissing(hashCodeNode)) {
        continue;
      }

      int hashCode = hashCodeNode.getAsInt();
      JsonObject segmentTemplate = segmentTemplateMap.get(hashCode);
      TripSegment segment = createSegmentFromTemplate(gson, rawSegment, segmentTemplate);
      segments.add(segment);
    }

    return segments;
  }

  private TripSegment createSegmentFromTemplate(Gson gson,
                                                JsonObject rawSegment,
                                                JsonObject segmentTemplate) {
    if (segmentTemplate != null) {
      Set<Map.Entry<String, JsonElement>> entrySet = segmentTemplate.entrySet();
      for (Map.Entry<String, JsonElement> entry : entrySet) {
        String key = entry.getKey();
        JsonElement value = entry.getValue();

        if (NODE_ACTION.equals(key)) {
          processSegmentTemplateAction(rawSegment, value);
        } else if (NODE_NOTES.equals(key)) {
          processSegmentTemplateNotes(rawSegment, value);
        } else {
          rawSegment.add(key, value);
        }
      }
    }

    TripSegment segment = gson.fromJson(rawSegment, TripSegment.class);
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

  private void processSegmentTemplateNotes(JsonObject rawSegment, JsonElement notesNode) {
    if (isElementMissing(notesNode)) {
      return;
    }

    String notes = notesNode.getAsString();
    if (!TextUtils.isEmpty(notes)) {
      JsonPrimitive serviceNameNode = rawSegment.getAsJsonPrimitive(NODE_SERVICE_NAME);
      if (!isElementMissing(serviceNameNode)) {
        final String serviceName = serviceNameNode.getAsString();
        if (serviceName != null) {
          // No need to check empty. Reason: https://redmine.buzzhives.com/issues/5803.
          notes = notes.replace(TEMPLATE_LINE_NAME, serviceName);
        }
      }

      // Replaces '<DIRECTION>' with segment's 'serviceDirection'
      notes = processDirectionTemplate(rawSegment.getAsJsonPrimitive(NODE_SERVICE_DIRECTION), notes);

      rawSegment.addProperty(NODE_NOTES, notes);
    }
  }

  private void processSegmentTemplateAction(JsonObject rawSegment, JsonElement actionNode) {
    if (isElementMissing(actionNode)) {
      return;
    }

    String action = actionNode.getAsString();
    if (!TextUtils.isEmpty(action)) {
      action = processActionNumber(rawSegment, action);
      rawSegment.addProperty(NODE_ACTION, action);
    }
  }

  private String processActionNumber(JsonObject rawSegment, String action) {
    JsonPrimitive serviceNumberNode = rawSegment.getAsJsonPrimitive(NODE_SERVICE_NUMBER);
    if (!isElementMissing(serviceNumberNode)) {
      String serviceNumber = serviceNumberNode.getAsString();
      if (!TextUtils.isEmpty(serviceNumber)) {
        action = action.replace(TEMPLATE_NUMBER, serviceNumber);
      } else if (action.contains(TEMPLATE_NUMBER)) {
        // Doesn't have service number but the template still exists.
        // We'll replace it with mode value instead.
        // 'Take <NUMBER>' will then become 'Take the bus' if the mode is 'bus'.
        JsonPrimitive modeNode = rawSegment.getAsJsonPrimitive("mode");
        if (!isElementMissing(modeNode)) {
          String mode = modeNode.getAsString();
          if (!TextUtils.isEmpty(mode)) {
            action = action.replace(TEMPLATE_NUMBER, "the " + mode);
          }

          // Else? if it takes place, call backend guys right away.
        }
      }
    }

    return action;
  }
}