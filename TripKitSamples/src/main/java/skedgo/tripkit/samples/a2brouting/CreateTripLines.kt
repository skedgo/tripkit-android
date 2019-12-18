package skedgo.tripkit.samples.a2brouting

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.skedgo.tripkit.LineSegment
import com.skedgo.tripkit.a2brouting.GetNonTravelledLineForTrip
import com.skedgo.tripkit.a2brouting.GetTravelledLineForTrip
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import com.skedgo.tripkit.routing.TripSegment

class CreateTripLines(private val getTravelledLineForTrip: GetTravelledLineForTrip,
                      private val getNonTravelledLineForTrip: GetNonTravelledLineForTrip) {
  fun execute(segments: List<TripSegment>): Observable<PolylineOptions> {
    return Observable.zip(getTravelledLineForTrip.execute(segments).toList().toObservable(),
        getNonTravelledLineForTrip.execute(segments).toList().toObservable(), BiFunction { travelled: List<List<LineSegment>>, nonTravelled: List<List<LineSegment>> -> travelled to nonTravelled })
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
        .flatMap { Observable.fromIterable(it) }
  }
}