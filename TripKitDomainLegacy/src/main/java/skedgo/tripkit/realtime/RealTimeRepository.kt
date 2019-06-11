package skedgo.tripkit.realtime

import com.skedgo.android.common.agenda.IRealTimeElement
import rx.Observable
import skedgo.tripkit.routing.RealTimeVehicle

interface RealTimeRepository {

  fun getUpdates(region: String, elements: List<IRealTimeElement>): Observable<List<RealTimeVehicle>>
}