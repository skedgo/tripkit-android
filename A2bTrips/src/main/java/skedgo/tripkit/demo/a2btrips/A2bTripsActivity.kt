package skedgo.tripkit.demo.a2btrips

import android.databinding.DataBindingUtil
import android.os.Bundle
import rx.android.schedulers.AndroidSchedulers.mainThread
import skedgo.rxlifecyclecomponents.RxAppCompatActivity
import skedgo.rxlifecyclecomponents.bindToLifecycle
import skedgo.tripkit.demo.a2btrips.databinding.A2bTripsBinding

class A2bTripsActivity : RxAppCompatActivity() {
  val viewModel: A2bTripsViewModel by lazy { A2bTripsViewModel() }
  val binding: A2bTripsBinding by lazy {
    DataBindingUtil.setContentView<A2bTripsBinding>(this, R.layout.a2b_trips)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.progressLayout.isEnabled = false

    viewModel.progressVisibility
        .bindToLifecycle(this)
        .observeOn(mainThread())
        .subscribe { binding.progressLayout.visibility = it }
  }
}
