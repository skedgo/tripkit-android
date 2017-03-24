package com.skedgo.android.tripkit.waypoints


import com.skedgo.android.common.model.Region
import com.skedgo.android.common.model.TripGroup
import com.skedgo.android.common.model.TripSegment
import com.skedgo.android.tripkit.TimetableEntry

import javax.inject.Inject

import rx.Single
import rx.functions.Action1

class GetTripsForChangingServiceImpl
@Inject
constructor(private val waypointService: WaypointService, private val waypointSegmentAdapterUtils: WaypointSegmentAdapterUtils) : GetTripByChangingService {

    override fun call(region: Region, segments: List<TripSegment>,
                      prototypeSegment: TripSegment,
                      service: TimetableEntry,
                      configurationParams: ConfigurationParams): Single<List<TripGroup>> {
        val waypointSegments = waypointSegmentAdapterUtils.adaptServiceSegmentList(prototypeSegment, service, region,
                segments)

        return Single
                .create { singleSubscriber ->
                    waypointService.fetchChangedTripAsync(configurationParams, waypointSegments)
                            .subscribe(Action1<List<TripGroup>> { tripGroups -> singleSubscriber.onSuccess(tripGroups) },
                                    Action1<Throwable> { throwable -> singleSubscriber.onError(throwable) })
                }
    }
}
