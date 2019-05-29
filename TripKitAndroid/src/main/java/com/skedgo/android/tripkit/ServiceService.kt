package com.skedgo.android.tripkit

import rx.Observable
import skedgo.tripkit.routing.Shape

interface ServiceService {

  fun getServiceShapes(region: String, tripId: String, timeInSecs: Long): Observable<List<Shape>>
}

internal class ServiceServiceImpl (
    private val serviceApi: ServiceApi
) : ServiceService {

  override fun getServiceShapes(region: String, tripId: String, timeInSecs: Long): Observable<List<Shape>> =
      serviceApi.getServiceAsync(region, tripId, timeInSecs, true)
          .map { it.shapes() }
}