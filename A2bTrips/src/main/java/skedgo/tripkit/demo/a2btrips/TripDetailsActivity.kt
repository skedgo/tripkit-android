package skedgo.tripkit.demo.a2btrips

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
import com.skedgo.android.common.model.Trip
import com.skedgo.android.tripkit.TripLinesProcessor
import rx.Observable
import skedgo.tripkit.demo.a2btrips.databinding.ActivityTripDetailsBinding

class TripDetailsActivity : AppCompatActivity() {

  companion object {
    private val TRIP_EXTRA = "TripExtra"

    fun newIntent(context: Context, trip: Trip): Intent {
      val intent = Intent(context, TripDetailsActivity::class.java)
      intent.putExtra(TRIP_EXTRA, trip)
      return intent
    }
  }

  val trip: Trip by lazy { intent.getParcelableExtra<Trip>(TRIP_EXTRA) }

  val viewModel by lazy { TripDetailsViewModel(this, trip) }
  val tripLinesProcessor = TripLinesProcessor()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = DataBindingUtil.setContentView<ActivityTripDetailsBinding>(this, R.layout.activity_trip_details)
    binding.viewModel = viewModel
    val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
    mapFragment.getMapAsync { map ->
      tripLinesProcessor.getPolylineOptionsListAsync(trip.segments)
          .flatMap { Observable.from(it) }
          .subscribe { map.addPolyline(it) }
      val height = resources.displayMetrics.heightPixels;
      val width = resources.displayMetrics.widthPixels;

      map.animateCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds.Builder()
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
    val bottomSheet = BottomSheetBehavior.from(binding.bottomSheet)
    bottomSheet.peekHeight = 360
  }
}