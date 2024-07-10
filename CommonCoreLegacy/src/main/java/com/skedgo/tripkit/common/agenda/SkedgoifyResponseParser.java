package com.skedgo.tripkit.common.agenda;

import android.content.res.Resources;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.skedgo.tripkit.routing.RoutingResponse;
import com.skedgo.tripkit.routing.TripGroup;

import org.json.JSONException;

import java.util.ArrayList;

public class SkedgoifyResponseParser {
    private Gson gson;
    private Resources resources;

    public SkedgoifyResponseParser(Resources resources, Gson gson) {
        this.gson = gson;
        this.resources = resources;
    }

    public BigAlgorithmResponse parse(String responseText) throws Exception {
        BigAlgorithmResponse response = gson.fromJson(responseText, BigAlgorithmResponse.class);
        resolve(response);
        return response;
    }

    private void resolve(BigAlgorithmResponse response) throws Exception {
        BigAlgorithmResult result = response.result();
        if (result != null) {
            ArrayList<JsonObject> segmentTemplateList = result.segmentTemplates();
            RoutingResponse parser = new RoutingResponse();
            SparseArray<JsonObject> segmentTemplateMap = parser.createSegmentTemplateMap(segmentTemplateList);

            ArrayList<JsonObject> trackItemJsonList = result.track();
            try {
                ArrayList<TrackItem> trackItemList = parseTrackItemList(trackItemJsonList, segmentTemplateMap);
                response.setTrackItems(trackItemList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * (Tung) I cannot parse this json array using Gson
     * because Gson requires the format of the object being known beforehand,
     * which is not the case in this array: an object
     * might be of class "event" or "trip".
     * So the parsing must be done manually.
     */
    private ArrayList<TrackItem> parseTrackItemList(ArrayList<JsonObject> trackItemJsonList,
                                                    SparseArray<JsonObject> segmentTemplateMap)
        throws Exception {
        ArrayList<TrackItem> trackItemList = new ArrayList<TrackItem>();

        RoutingResponse parser = new RoutingResponse();
        for (JsonObject trackItemJson : trackItemJsonList) {
            JsonPrimitive trackItemClass = trackItemJson.getAsJsonPrimitive("class");
            String className = trackItemClass.getAsString();
            if (className.equals("event")) {
                EventTrackItem eventItem = gson.fromJson(trackItemJson, EventTrackItem.class);
                trackItemList.add(eventItem);
            } else if (className.equals("trip")) {
                TripTrackItem tripItem = gson.fromJson(trackItemJson, TripTrackItem.class);
                ArrayList<TripGroup> tripGroupList = tripItem.getGroups();
                tripGroupList = parser.processRawData(resources, gson, tripGroupList, segmentTemplateMap);

                tripItem.setGroups(tripGroupList);
                trackItemList.add(tripItem);
            } else {
                throw new JSONException("Unrecognized track item");
            }
        }

        return trackItemList;
    }
}