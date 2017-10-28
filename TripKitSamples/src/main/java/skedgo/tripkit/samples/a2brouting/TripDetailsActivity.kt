package skedgo.tripkit.samples.a2brouting

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import rx.Observable
import skedgo.tripkit.android.TripKit
import skedgo.tripkit.routing.Trip
import skedgo.tripkit.samples.R
import skedgo.tripkit.samples.databinding.TripDetailsBinding

class TripDetailsActivity : AppCompatActivity() {
  companion object {
    private val tripKey = "trip"

    fun newIntent(context: Context, trip: Trip): Intent
        = Intent(context, TripDetailsActivity::class.java)
        .putExtra(tripKey, trip.id)
  }

  val trip: Trip? by lazy {
    intent.getLongExtra(tripKey, 0).let { TripGroupRepository.getTrip(it) }
  }

  val viewModel by lazy { trip?.let { TripDetailsViewModel(this, it) } }
  val getTripLine = TripKit.getInstance().a2bRoutingComponent().getTripLine

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = DataBindingUtil.setContentView<TripDetailsBinding>(this, R.layout.trip_details)
    binding.viewModel = viewModel

    trip?.let { trip ->
      val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
      mapFragment.getMapAsync { map ->
        getTripLine.execute(trip.segments)
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
