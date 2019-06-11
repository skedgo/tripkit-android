package skedgo.tripkit.realtime

import com.jakewharton.rxrelay.PublishRelay
import com.skedgo.android.common.model.RealtimeAlert
import rx.Observable
import javax.inject.Inject

class RealtimeAlertRepositoryImpl @Inject constructor() : RealtimeAlertRepository {

  private val alerts = mutableMapOf<Long, RealtimeAlert>()
  private val alertsHashCodeMap = mutableMapOf<String, List<Long>>()
  private val whenRealtimeAlertAdded = PublishRelay.create<Pair<Long, RealtimeAlert>>()

  override fun addAlerts(alerts: List<RealtimeAlert>) {
    alerts.forEach {
      this.alerts.put(it.remoteHashCode(), it)
      whenRealtimeAlertAdded.call(Pair(it.remoteHashCode(), it))
    }
  }

  override fun addAlertHashCodesForId(id: String, alertHashCodes: List<Long>) {
    alertsHashCodeMap[id] = alertHashCodes
  }

  override fun getAlerts(id: String): List<RealtimeAlert>? =
      alertsHashCodeMap[id]?.mapNotNull { alerts[it] }

  override fun onAlertForIdAdded(id: String): Observable<RealtimeAlert> =
      whenRealtimeAlertAdded.asObservable()
          .filter { (hashCode, _) ->
            alertsHashCodeMap[id]?.contains(hashCode) ?: false
          }
          .map { (_, alert) -> alert }
}