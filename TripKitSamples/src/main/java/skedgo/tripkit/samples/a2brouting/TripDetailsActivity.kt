package skedgo.tripkit.samples.a2brouting

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import rx.Observable
import skedgo.tripkit.a2brouting.GetNonTravelledLineForTrip
import skedgo.tripkit.a2brouting.GetTravelledLineForTrip
import skedgo.tripkit.routing.Trip
import skedgo.tripkit.samples.R
import skedgo.tripkit.samples.databinding.TripDetailsBinding

class TripDetailsActivity : AppCompatActivity() {
  companion object {
    private val tripKey = "trip"

    fun newIntent(context: Context, trip: Trip): Intent = Intent(context, TripDetailsActivity::class.java)
        .putExtra(tripKey, trip.id)
  }

  val trip: Trip? by lazy {
    intent.getLongExtra(tripKey, 0).let { TripGroupRepository.getTrip(it) }
  }

  val viewModel by lazy { trip?.let { TripDetailsViewModel(this, it) } }
  val getNonTravelledLineForTrip = GetNonTravelledLineForTrip()

  val getTravelledLineForTrip = GetTravelledLineForTrip()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val binding = DataBindingUtil.setContentView<TripDetailsBinding>(this, R.layout.trip_details)
    binding.viewModel = viewModel

    trip?.let { trip ->
      val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
      mapFragment.getMapAsync { map ->
        Observable.zip(getTravelledLineForTrip.execute(trip.segments).toList(),
            getNonTravelledLineForTrip.execute(trip.segments).toList()) { travelled, nonTravelled -> travelled to nonTravelled }
            .map { (travelled, nonTravelled) ->
              val travelledPolyLines =
                  travelled.map {
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

              val nonTravelledPolyLines =
                  nonTravelled
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
            .subscribe { map.addPolyline(it) }
        val height = resources.displayMetrics.heightPixels
        val width = resources.displayMetrics.widthPixels

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds.Builder()
            .include(LatLng(trip.from.lat, trip.from.lon))
            .include(LatLng(trip.to.lat, trip.to.lon))
            .build(), width, height, height / 10))

        map.addMarker(MarkerOptions()
            .position(LatLng(trip.from.lat, trip.from.lon))
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
        map.addMarker(MarkerOptions()
            .position(LatLng(trip.to.lat, trip.to.lon))
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
      }
    }
    val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
    bottomSheetBehavior.peekHeight = 360
  }
}