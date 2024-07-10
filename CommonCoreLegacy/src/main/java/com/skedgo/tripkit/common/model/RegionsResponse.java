package com.skedgo.tripkit.common.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

public class RegionsResponse {
    @SerializedName("regions")
    private ArrayList<Region> regions;
    @SerializedName("modes")
    private Map<String, TransportMode> transportModeMap;

    @Nullable
    public ArrayList<Region> getRegions() {
        return regions;
    }

    @VisibleForTesting
    void setRegions(ArrayList<Region> regions) {
        this.regions = regions;
    }

    @Nullable
    public Collection<TransportMode> getTransportModes() {
        correctModeIds();
        return transportModeMap != null
            ? transportModeMap.values()
            : null;
    }

    @VisibleForTesting
    void setTransportModeMap(Map<String, TransportMode> transportModeMap) {
        this.transportModeMap = transportModeMap;
    }

    private void correctModeIds() {
        if (transportModeMap != null) {
            for (Map.Entry<String, TransportMode> entry : transportModeMap.entrySet()) {
                if (entry.getValue() != null) {
                    entry.getValue().setId(entry.getKey());
                }
            }
        }
    }
}