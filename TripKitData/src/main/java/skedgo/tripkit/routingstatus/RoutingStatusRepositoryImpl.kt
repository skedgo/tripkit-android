package skedgo.tripkit.routingstatus

import com.jakewharton.rxrelay.PublishRelay
import com.skedgo.routepersistence.RoutingStatusStore
import com.skedgo.routepersistence.toDto
import com.skedgo.routepersistence.toStatus
import rx.Completable
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoutingStatusRepositoryImpl @Inject constructor(
    private val routingStatusStore: RoutingStatusStore
) : RoutingStatusRepository {
  private val routingStatusUpdates: PublishRelay<String> = PublishRelay.create()

  override fun getRoutingStatus(requestId: String): Observable<RoutingStatus> =
      routingStatusStore
          .getLastStatus(requestId)
          .map { RoutingStatus(requestId, it.first.toStatus(it.second)) }
          .toObservable()
          .repeatWhen {
            routingStatusUpdates.asObservable().filter { it == requestId }
          }

  override fun putRoutingStatus(routingStatus: RoutingStatus): Completable {
    val status = routingStatus.status
    val message: String? = if (status is Status.Error) {
      status.message
    } else {
      null
    }
    return routingStatusStore
        .updateStatus(routingStatus.routingRequestId, status.toDto(), message)
        .andThen(Completable.fromAction {
          routingStatusUpdates.call(routingStatus.routingRequestId)
        })
  }
}
