package com.skedgo.android.tripkit.waypoints;

import com.skedgo.android.common.model.TripGroup;

import java.util.List;

import rx.Observable;

public interface WaypointService {

  Observable<List<TripGroup>> fetchChangedTripAsync(ConfigurationParams config, List<WaypointSegmentAdapter> segments);
}
