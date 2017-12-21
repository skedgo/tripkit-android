package skedgo.tripkit.routingstatus

import rx.Observable
import javax.inject.Inject

open class HasRoutingError @Inject internal constructor(
    private val routingStatusRepository: RoutingStatusRepository
) {
  open fun execute(routingRequestId: Observable<String>): Observable<Pair<Boolean, String>>
      = routingRequestId.flatMap { routingStatusRepository.getRoutingStatus(it) }
      .map {
        when (it.status) {
          is Status.Error -> Pair(true, it.status.message)
          else -> Pair(false, "")
        }
      }
}
