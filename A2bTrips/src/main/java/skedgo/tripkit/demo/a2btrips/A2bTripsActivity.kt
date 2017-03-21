package skedgo.tripkit.demo.a2btrips

import android.databinding.DataBindingUtil
import android.os.Bundle

import skedgo.rxlifecyclecomponents.RxAppCompatActivity
import skedgo.tripkit.demo.a2btrips.databinding.A2bTripsBinding

class A2bTripsActivity : RxAppCompatActivity() {
  val binding by lazy {
    DataBindingUtil.setContentView<A2bTripsBinding>(this, R.layout.a2b_trips)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }
}
