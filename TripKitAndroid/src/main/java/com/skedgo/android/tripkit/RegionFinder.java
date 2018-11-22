package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.util.PolyUtil;
import com.skedgo.android.common.util.TripKitLatLng;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class RegionFinder {
  private final Map<String, List<TripKitLatLng>> polygonCache = new ConcurrentHashMap<>();

  public boolean contains(Region region, double lat, double lng) {
    final List<TripKitLatLng> polygon = getPolygon(region, polygonCache);
    return polygon != null
        && PolyUtil.containsLocation(lat, lng, polygon, true);
  }

  public void invalidate() {
    polygonCache.clear();
  }

  private List<TripKitLatLng> getPolygon(Region region, Map<String, List<TripKitLatLng>> polygonCache) {
    final String name = region.getName();
    List<TripKitLatLng> polygon = polygonCache.get(name);
    if (polygon == null) {
      polygon = PolyUtil.decode(region.getEncodedPolyline());
      polygonCache.put(name, polygon);
    }
    return polygon;
  }
}
