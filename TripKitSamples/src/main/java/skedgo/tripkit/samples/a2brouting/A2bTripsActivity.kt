package skedgo.tripkit.samples.a2brouting

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import skedgo.rxlifecyclecomponents.RxAppCompatActivity
import skedgo.rxlifecyclecomponents.bindToLifecycle
import skedgo.tripkit.android.TripKit
import skedgo.tripkit.samples.R
import skedgo.tripkit.samples.databinding.A2bTripsBinding
import java.util.concurrent.TimeUnit

class A2bTripsActivity : RxAppCompatActivity() {
  val viewModel: A2bTripsViewModel by lazy {
    A2bTripsViewModel(
        applicationContext,
        TripKit.getInstance().routeService
    )
  }
  val binding: A2bTripsBinding by lazy {
    DataBindingUtil.setContentView<A2bTripsBinding>(this, R.layout.a2b_trips)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.viewModel = viewModel
    binding.progressLayout.isEnabled = false

    viewModel.isRefreshing
        .bindToLifecycle(this)
        .observeOn(mainThread())
        .subscribe { binding.progressLayout.isRefreshing = it }

    Observable.just(Unit)
        .delay(500, TimeUnit.MILLISECONDS)
        .flatMap { viewModel.showSampleTrips() }
        .bindToLifecycle(this)
        .subscribe({}, {
          AlertDialog.Builder(this)
              .setTitle("Error while routing")
              .setMessage(it.message)
              .create()
              .show()
        })
    viewModel.onTripSelected
        .map { TripDetailsActivity.newIntent(this, it) }
        .bindToLifecycle(this)
        .subscribe { startActivity(it) }
  }
}
