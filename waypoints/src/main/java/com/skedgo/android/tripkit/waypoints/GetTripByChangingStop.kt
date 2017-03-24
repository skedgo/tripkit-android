package com.skedgo.android.tripkit.waypoints

import com.skedgo.android.common.model.Location
import com.skedgo.android.common.model.TripGroup
import com.skedgo.android.common.model.TripSegment
import rx.Single
import rx.functions.Action1
import javax.inject.Inject

class GetTripsForChangingStopImpl
@Inject
constructor(private val waypointService: WaypointService, private val waypointSegmentAdapterUtils: WaypointSegmentAdapterUtils) : GetTripByChangingStop {

    override fun call(segments: List<TripSegment>,
                      prototypeSegment: TripSegment,
                      waypoint: Location,
                      isGetOn: Boolean,
                      configurationParams: ConfigurationParams): Single<List<TripGroup>> {

        val waypointSegments = waypointSegmentAdapterUtils.adaptStopSegmentList(prototypeSegment, waypoint, isGetOn, segments)

        return Single
                .create { singleSubscriber ->
                    waypointService.fetchChangedTripAsync(configurationParams, waypointSegments)
                            .subscribe(Action1<List<TripGroup>> { tripGroups -> singleSubscriber.onSuccess(tripGroups) },
                                    Action1<Throwable> { throwable -> singleSubscriber.onError(throwable) })
                }

    }

}
