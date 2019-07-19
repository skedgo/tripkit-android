package skedgo.tripkit.a2brouting

import com.skedgo.android.common.model.Query
import com.skedgo.android.tripkit.TransitModeFilter
import com.skedgo.android.tripkit.TransportModeFilter

import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.Subject
import skedgo.tripkit.routing.TripGroup

/**
 * A decorator of [RouteService] that performs only one routing request.
 * For example, if we ask it to route from A to B, and while that request
 * is still progress and later we ask it to route from C to B,
 * then the request A-to-B will be cancelled.
 * Cancellation is invoked asynchronously. That means the execution
 * of the request C-to-D doesn't have to wait for
 * the cancellation of the request A-to-B to be done to get started.
 */
class SingleRouteService(private val routeService: RouteService) : RouteService {
  /* toSerialized() to be thread-safe. */
  private val cancellationSignal = PublishSubject.create<Void>().toSerialized()

  override fun routeAsync(query: Query,
                          transportModeFilter: TransportModeFilter,
                          transitModeFilter: TransitModeFilter): Observable<List<TripGroup>> {
    cancellationSignal.onNext(null)
    return routeService.routeAsync(query, transportModeFilter, transitModeFilter)
        .takeUntil(cancellationSignal.asObservable())
  }
}