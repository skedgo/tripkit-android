package skedgo.tripkit.samples.a2brouting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.skedgo.tripkit.a2brouting.GetNonTravelledLineForTrip
import com.skedgo.tripkit.a2brouting.GetTravelledLineForTrip
import com.skedgo.tripkit.routing.Trip
import skedgo.tripkit.samples.R
import skedgo.tripkit.samples.databinding.TripDetailsBinding

class TripDetailsActivity : AppCompatActivity() {
    companion object {
        private val tripKey = "trip"

        fun newIntent(context: Context, trip: Trip): Intent =
            Intent(context, TripDetailsActivity::class.java)
                .putExtra(tripKey, trip.id)
    }

    val trip: Trip? by lazy {
        intent.getLongExtra(tripKey, 0).let { TripGroupRepository.getTrip(it) }
    }

    val viewModel by lazy { trip?.let { TripDetailsViewModel(this, it) } }
    val createTripLines = CreateTripLines(
        getTravelledLineForTrip = GetTravelledLineForTrip(),
        getNonTravelledLineForTrip = GetNonTravelledLineForTrip()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<TripDetailsBinding>(this, R.layout.trip_details)
        binding.viewModel = viewModel

        trip?.let { trip ->
            val mapFragment =
                supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
            mapFragment.getMapAsync { map ->
                createTripLines.execute(trip.segments)
                    .subscribe { map.addPolyline(it) }
                val height = resources.displayMetrics.heightPixels
                val width = resources.displayMetrics.widthPixels

                val builder = LatLngBounds.Builder().include(LatLng(trip.from.lat, trip.from.lon))

                trip.to?.let {
                    builder.include(LatLng(it.lat, it.lon))
                }

                map.moveCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        builder.build(), width, height, height / 10
                    )
                )

                map.addMarker(
                    MarkerOptions()
                        .position(LatLng(trip.from.lat, trip.from.lon))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                )

                trip.to?.let {
                    map.addMarker(
                        MarkerOptions()
                            .position(LatLng(it.lat, it.lon))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    )
                }

            }
        }
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.peekHeight = 360
    }
}