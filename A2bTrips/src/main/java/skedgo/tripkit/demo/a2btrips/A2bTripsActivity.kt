package skedgo.tripkit.demo.a2btrips

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import skedgo.rxlifecyclecomponents.RxAppCompatActivity
import skedgo.rxlifecyclecomponents.bindToLifecycle
import skedgo.tripkit.demo.a2btrips.databinding.A2bTripsBinding
import java.util.concurrent.TimeUnit

class A2bTripsActivity : RxAppCompatActivity() {
  val viewModel: A2bTripsViewModel by lazy { A2bTripsViewModel(applicationContext) }
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
        .bindToLifecycle(this)
        .subscribe({
          val intent = TripDetailsActivity.newIntent(this, it)
          startActivity(intent)
        })
  }
}
