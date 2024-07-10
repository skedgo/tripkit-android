package com.skedgo.tripkit.common.agenda;

import com.google.gson.annotations.SerializedName;
import com.skedgo.tripkit.routing.TripGroup;

import java.util.ArrayList;

public class TripTrackItem extends TrackItem {
    @SerializedName("groups")
    private ArrayList<TripGroup> groups;
    @SerializedName("fromId")
    private String fromId;
    @SerializedName("toId")
    private String toId;

    public ArrayList<TripGroup> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<TripGroup> groups) {
        this.groups = groups;
    }

    public String fromId() {
        return fromId;
    }

    public String toId() {
        return toId;
    }
}