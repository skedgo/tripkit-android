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

  override fun getRoutingStatus(requestId: String): Observable<RoutingStatus> {
    return routingStatusStore.getLastStatus(requestId)
        .map { RoutingStatus(requestId, it.toStatus()) }
        .toObservable()
        .repeatWhen {
          routingStatusUpdates.asObservable().filter { it == requestId }
        }
  }

  override fun putRoutingStatus(routingStatus: RoutingStatus): Completable {
    return routingStatusStore.updateStatus(routingStatus.routingRequestId, routingStatus.status.toDto())
        .andThen(Completable.fromAction {
          this.routingStatusUpdates.call(routingStatus.routingRequestId)
        })
  }
}
