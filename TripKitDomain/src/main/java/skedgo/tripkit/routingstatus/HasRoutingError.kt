package skedgo.tripkit.routingstatus

import rx.Observable
import javax.inject.Inject

open class HasRoutingError @Inject internal constructor(
    private val routingStatusRepository: RoutingStatusRepository
) {
  open fun execute(routingRequestId: Observable<String>): Observable<Boolean>
      = routingRequestId.flatMap { routingStatusRepository.getRoutingStatus(it) }
      .map {
        when (it.status) {
          is Status.Error -> true
          else -> false
        }
      }
}
