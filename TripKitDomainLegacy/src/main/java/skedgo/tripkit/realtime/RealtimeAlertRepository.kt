package skedgo.tripkit.realtime

import com.skedgo.android.common.model.RealtimeAlert
import rx.Observable

interface RealtimeAlertRepository {

  fun addAlerts(alerts: List<RealtimeAlert>)

  fun addAlertHashCodesForId(id: String, alertHashCodes: List<Long>)

  fun getAlerts(id: String): List<RealtimeAlert>?

  fun onAlertForIdAdded(id: String): Observable<RealtimeAlert>
}