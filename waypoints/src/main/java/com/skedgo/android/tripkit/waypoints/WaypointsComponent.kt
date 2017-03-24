package com.skedgo.android.tripkit.waypoints

import com.skedgo.android.common.model.Location
import com.skedgo.android.common.model.Region
import com.skedgo.android.common.model.TripGroup
import com.skedgo.android.common.model.TripSegment
import com.skedgo.android.tripkit.TimetableEntry
import rx.Single

interface GetTripByChangingService {
    fun call(region: Region, segments: List<TripSegment>,
             prototypeSegment: TripSegment,
             service: TimetableEntry,
             configurationParams: ConfigurationParams): Single<List<TripGroup>>
}

interface GetTripByChangingStop {
    fun call(segments: List<TripSegment>,
             prototypeSegment: TripSegment,
             stopToChange: Location, isGetOn: Boolean,
             configurationParams: ConfigurationParams): Single<List<TripGroup>>
}

interface WaypointsComponent {
    fun getTripByChangingService(): GetTripByChangingService
    fun getTripByChangingStop(): GetTripByChangingStop
}
