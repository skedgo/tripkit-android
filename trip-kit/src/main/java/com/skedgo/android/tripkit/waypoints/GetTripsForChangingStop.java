package com.skedgo.android.tripkit.waypoints;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.common.model.TripSegment;

import java.util.ArrayList;
import java.util.List;

import rx.Single;

public interface GetTripsForChangingStop {

  Single<List<TripGroup>> getTrips(ArrayList<TripSegment> segments,
                                   TripSegment prototypeSegment,
                                   Location waypoint,
                                   boolean isGetOn,
                                   ConfigurationParams configurationParams);
}
