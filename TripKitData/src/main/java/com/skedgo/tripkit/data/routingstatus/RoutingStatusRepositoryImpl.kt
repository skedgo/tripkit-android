package com.skedgo.tripkit.data.routingstatus

import com.jakewharton.rxrelay2.PublishRelay
import com.skedgo.routepersistence.RoutingStatusStore
import com.skedgo.routepersistence.toDto
import com.skedgo.routepersistence.toStatus
import com.skedgo.tripkit.routingstatus.RoutingStatus
import com.skedgo.tripkit.routingstatus.RoutingStatusRepository
import com.skedgo.tripkit.routingstatus.Status
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

private val routingStatusUpdates: PublishRelay<String> = PublishRelay.create()

@Singleton
class RoutingStatusRepositoryImpl @Inject constructor(
    private val routingStatusStore: RoutingStatusStore
) : RoutingStatusRepository {

    override fun getRoutingStatus(requestId: String): Observable<RoutingStatus> =
        routingStatusStore
            .getLastStatus(requestId)
            .map { RoutingStatus(requestId, it.first.toStatus(it.second)) }
            .toObservable()
            .repeatWhen {
                routingStatusUpdates.filter { it == requestId }
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
                routingStatusUpdates.accept(routingStatus.routingRequestId)
            })
    }
}
