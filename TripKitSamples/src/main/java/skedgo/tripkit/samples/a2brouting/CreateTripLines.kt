package skedgo.tripkit.samples.a2brouting

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import rx.Observable
import skedgo.tripkit.a2brouting.GetNonTravelledLineForTrip
import skedgo.tripkit.a2brouting.GetTravelledLineForTrip
import skedgo.tripkit.routing.TripSegment

class CreateTripLines(private val getTravelledLineForTrip: GetTravelledLineForTrip,
                      private val getNonTravelledLineForTrip: GetNonTravelledLineForTrip) {
  fun execute(segments: List<TripSegment>): Observable<PolylineOptions> {
    return Observable.zip(getTravelledLineForTrip.execute(segments).toList(),
        getNonTravelledLineForTrip.execute(segments).toList()) { travelled, nonTravelled -> travelled to nonTravelled }
        .map { (travelled, nonTravelled) ->
          val travelledPolyLines = travelled.map {
            val latLngList = it
                .flatMap {
                  listOf(it.start, it.end)
                }
                .map { LatLng(it.latitude, it.longitude) }

            PolylineOptions()
                .addAll(latLngList)
                .color(it.first().color)
                .width(7f)
          }

          val nonTravelledPolyLines = nonTravelled
              .map {
                val latLngList = it
                    .flatMap {
                      listOf(it.start, it.end)
                    }
                    .map { LatLng(it.latitude, it.longitude) }

                PolylineOptions()
                    .addAll(latLngList)
                    .color(Color.parseColor("#88AAAAAA"))
                    .width(7f)
              }
          travelledPolyLines + nonTravelledPolyLines
        }
        .flatMap { Observable.from(it) }
  }
}