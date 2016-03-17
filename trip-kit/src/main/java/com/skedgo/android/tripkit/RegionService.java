package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.ModeInfo;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.TransportMode;

import java.util.List;
import java.util.Map;

import rx.Observable;

public interface RegionService {
  Observable<List<Region>> getRegionsAsync();
  Observable<Region> getRegionByLocationAsync(@Nullable Location location);
  Observable<Location> getCitiesAsync();
  Observable<Location> getCitiesByNameAsync(@Nullable String name);
  Observable<Map<String, TransportMode>> getTransportModesAsync();
  Observable<List<TransportMode>> getTransportModesByIdsAsync(@NonNull List<String> modeIds);
  Observable<List<TransportMode>> getTransportModesByRegionAsync(@NonNull Region region);
  Observable<List<TransportMode>> getTransportModesByLocationAsync(@NonNull Location location);
  Observable<Void> refreshAsync();

  Observable<RegionInfo> getRegionInfoByRegionAsync(@NonNull Region region);
  Observable<Paratransit> fetchParatransitByRegionAsync(@NonNull Region region);
  Observable<List<ModeInfo>> getTransitModesByRegionAsync(@NonNull Region region);
}