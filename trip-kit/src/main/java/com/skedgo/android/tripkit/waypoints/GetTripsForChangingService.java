package com.skedgo.android.tripkit.waypoints;

import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.common.model.TripSegment;
import com.skedgo.android.tripkit.TimetableEntry;

import java.util.List;

import rx.Single;

public interface GetTripsForChangingService {

  Single<List<TripGroup>> getTrips(Region region, List<TripSegment> segments,
                                   TripSegment prototypeSegment,
                                   TimetableEntry service,
                                   ConfigurationParams configurationParams);
}
