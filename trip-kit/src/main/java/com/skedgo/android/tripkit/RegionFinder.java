package com.skedgo.android.tripkit;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.skedgo.android.common.model.Region;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class RegionFinder {
  private final Map<String, List<LatLng>> polygonCache = new ConcurrentHashMap<>();

  public boolean contains(Region region, double lat, double lng) {
    final List<LatLng> polygon = getPolygon(region, polygonCache);
    return polygon != null
        && PolyUtil.containsLocation(new LatLng(lat, lng), polygon, true);
  }

  public void invalidate() {
    polygonCache.clear();
  }

  private List<LatLng> getPolygon(Region region, Map<String, List<LatLng>> polygonCache) {
    final String name = region.getName();
    List<LatLng> polygon = polygonCache.get(name);
    if (polygon == null) {
      polygon = PolyUtil.decode(region.getEncodedPolyline());
      polygonCache.put(name, polygon);
    }
    return polygon;
  }
}
