package skedgo.tripkit.routingstatus

import rx.Observable
import javax.inject.Inject
import com.gojuno.koptional.*

open class HasRoutingError @Inject internal constructor(
    private val routingStatusRepository: RoutingStatusRepository
) {
  open fun execute(routingRequestId: Observable<String>): Observable<Optional<Status.Error>>
      = routingRequestId.flatMap { routingStatusRepository.getRoutingStatus(it) }
      .map {
        when (it.status) {
          is Status.Error -> Some(it.status)
          else -> None
        }
      }
}
