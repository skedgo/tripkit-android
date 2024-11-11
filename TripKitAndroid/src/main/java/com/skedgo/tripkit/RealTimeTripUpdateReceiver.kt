package com.skedgo.tripkit

import com.skedgo.tripkit.routing.Trip
import com.skedgo.tripkit.routing.TripGroup
import io.reactivex.Flowable


interface RealTimeTripUpdateReceiver {
    fun startAsync(): Flowable<Pair<Trip, TripGroup>>

    fun stop()
}