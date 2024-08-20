package com.skedgo.tripkit.data.locations;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.skedgo.tripkit.common.model.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class LocationsRequestBody {
    @SerializedName("region")
    public final String regionName;
    @SerializedName("cellIDs")
    public final ArrayList<String> cellIds;
    @SerializedName("level")
    public final int level;
    @SerializedName("cellIDHashCodes")
    public final Map<String, Long> existingCells;
    @SerializedName("config")
    public final JsonObject config;
    @SerializedName("modes")
    public final List<String> modes;

    private LocationsRequestBody(String regionName,
                                 ArrayList<String> cellIds,
                                 int level,
                                 Map<String, Long> existingCells,
                                 JsonObject config,
                                 List<String> modes) {
        this.regionName = regionName;
        this.cellIds = cellIds;
        this.level = level;
        this.existingCells = existingCells;
        this.config = config;
        this.modes = modes;
    }

    public static LocationsRequestBody createForNewlyFetching(@NonNull Region region,
                                                              @NonNull ArrayList<String> cellIds,
                                                              int level,
                                                              @Nullable JsonObject config) {
        return new LocationsRequestBody(region.getName(), cellIds, level, null, config, region.getTransportModeIds());
    }

    public static LocationsRequestBody createForUpdating(@NonNull Region region,
                                                         @NonNull Map<String, Long> existingCells,
                                                         int level,
                                                         @Nullable JsonObject config) {
        return new LocationsRequestBody(region.getName(), null, level, existingCells, config, region.getTransportModeIds());
    }
}